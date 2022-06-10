<h1>Coding Exercise</h1>
By: Neil Gilbert Gallardo

Exercise Requirements: (condensed from the Exercise document)
* Uses JSON (according to the Sample Log) as its data delivery method.
* Should utilize Spring Boot, Flask, etc. frameworks as its primary deployment method.
* Can utilize Java, Kotlin or Python to handle its logic.
* Ability to consume request to create log files in the system
* Ability to search for logs in the system and return them to the user.
* Search functionality should include searching by user, time range and log type.

Project Specifications:
* Frameworks used: Spring Boot api for deployment, Spring Boot Tools for live deployment
* Languages used: Java
* Data Transport Mode: JSON
* IDE used: IntelliJ IDE

Documentations:
* Javadoc: 
https://github.com/neilgilbertg/CodingExerciseNggApplication/tree/master/javadoc
* REST API documentation:
  https://neil-gallardo.stoplight.io/docs/codingexercisenggapplication/7458e990d7040-load-multiple-logs


Evaluation Considerations
* Is your code well tested?  
  * Somewhat, the API calls are tested withing the expected inputs.
* Is the service performant for the use cases provided?
  * Yes, it fulfills most of the requests of the exercise.
* Is the data stored efficiently?
  * No, it is stored in memory through json files in the server a more robust storage (i.e. Database) would be desired.
* Do you provide error handling and appropriate REST status codes?
  * Yes, the API responds to request.