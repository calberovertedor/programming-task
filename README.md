# programming-task
Programming task project using the [JSON Placeholder API](https://jsonplaceholder.typicode.com/).
### Author
- Carlos Albero Vertedor
### Description
This REST service developed with Java (Spring Boot) retrieves the data provided by the API from two different resources: Albums and Users. The following actions can be performed by calling the appropriate controller of each of the resources:

    - Get All Albums
    - Get All Users
    - Get Album By ID
    - Get User By ID
    - Get Album By Title
    - Get User By Name
    - Save Album By ID (XML and JSON)
    - Save User By ID (XML and JSON)
    - Create Album
    - Create User
    - Update Album
    - Update User
    - Delete Album By ID
    - Delete User By ID
    
The project uses RestTemplate to make the different HTTP requests to the API, Jackson's ObjectMapper to convert the Resources to JSON and JAXB to marshall them into XML files. Even though Jackson can also be used to convert to XML with XmlMapper, I've decided to use JAXB to avoid configuration issues between ObjectMapper and XmlMapper.
    
To facilitate the manual testing of this service, a Postman collection in JSON format with request examples of all the actions can be found in the following link: https://www.getpostman.com/collections/35002f0f23ea30bc311b

### Improvements
To further improve the project I would include validations of the most important fields of each resource to avoid NULL and empty values, test the controllers with Spring's MockMvc and update the JUnit tests of the services with more use cases.

Taking a different aproach, a front end side of the project could have been implemented using Thymeleaf to view the data and perform the different actions from a user interface. The data extracted from the API could have also been stored in a database and mapped to entities with JPA relationships.
