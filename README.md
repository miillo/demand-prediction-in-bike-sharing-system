# demand-prediction-in-bike-sharing-system
The project was developed for university course _Multi-agent Systems_. The purpose of this project was to implement 
multi-agent, distributed system for demand prediction in bike sharing system. 

## System architecture
[Architecture](src/main/resources/sys_architecture.PNG)

## Building & Running
User may adjust program setting by modifying application.conf file. System is started by sending HTTP POST request.
Example request is presented below. 
```
{
	"station-id": "3203",
	"start-date": "2019-09-01",
	"end-date": "2019-09-30"
}
```

## Data
Project data for demand prediction are downloaded from following sources:
* Bike trips / station data - http://api.citybik.es/v2/
* Weather data - https://api.meteostat.net/

## Used technologies
Project was dveloped using Java language with following technologies:
* Multi-agent system - Akka 2.5  
* Prediction module - Weka 3 