Feature: singleProject
    Prueba integral servicio

    Background:
        * def urlbase = defUrlBase
        * def datos = read(defPathDataError)
        * def datosSuccess = read(defPathDataSuccess)
        * def path = '/v1/operations/product-specific/transactional/basepath/servicepath'
        * def sslJSONcucumber = sslJSON

    Scenario Outline: Flujo de Afiliacion exitosa <description>.
        * configure headers = {'accept':'application/vnd.bancolombia.v4+json','message-id':'2398-2343-324233-32423424','id-consumer':'pruebasac','service-operation':'capatransaccional','service-namespace':'aceptacion','Content-Type':'application/json'}
        * configure ssl = sslJSONcucumber
        * header Authorization = authToken
        Given url urlbase + path
        And request  {"name":<nameFruit>,"description":<descriptionFruit>}
        When method POST
        Then status 200
        And match response contains ['#notnull']
        Examples:
            | datosSuccess |



    Scenario Outline: Errores de Negocio <description>
        * def json = {"name":<nameFruit>,"description":<descriptionFruit>}
        * configure ssl = sslJSONcucumber
        * configure headers = {'accept':'application/vnd.bancolombia.v4+json','message-id':'2398-2343-324233-32423424','id-consumer':'pruebasac','service-operation':'capatransaccional','service-namespace':'aceptacion','Content-Type':'application/json'}
        * header Authorization = authTokenError
        Given url urlbase + path
        And request json
        When method POST
        Then status <httpStatus>
        And match each $.errors.[*].code == '<bussStatus>'
        And match $.title == '<title>'
        And match $.status == '<httpStatus>'
        Examples:
            | datos |

    Scenario Outline: Validación formato del campo <description>
        * def json = {"name":<nameFruit>,"description":<descriptionFruit>}
        * configure ssl = sslJSONcucumber
        * configure headers = {'accept':'application/vnd.bancolombia.v4+json','message-id':'2398-2343-324233-32423424','id-consumer':'pruebasac','service-operation':'setupdebit','service-namespace':'aceptacion','Content-Type':'application/json'}
        * header Authorization = authToken
        Given url urlbase + path
        And request json
        When method POST
        Then status <httpstatus>
        Then match $.status == '<status>'
        Then match $.title == '<result>'
        Examples:
        | nameFruit        | descriptionFruit| result   | status  | httpstatus |
        | FruitOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOr | Algun texto Aca  | Nombre muy largo   |  4000010 | 400   |
        | FruitOrange | TropicalesOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrangeOrange  | Descripcion muy larga   |  4000010 | 400   |