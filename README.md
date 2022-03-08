# WhereMoney Webservice

## Installation guide
The easiest way to install the webservice is by using Docker.

## Docker installation:
**Clone the project**
```shell
git clone https://github.com/Asrez/WhereMoneyAPI.git
```  

**Change directory**
```shell
cd WhereMoneyAPI
```

**Open up `.env` file to set env variables**
```shell
vi ./.env
```
![.env file.](https://i.postimg.cc/VL8mmJKJ/Screenshot-from-2022-02-28-21-37-03.png)
> :warning: **Make sure to change sensitive data in .env file or your data might be as risk...**  


**docker-composer**
```shell
docker-compose up
```
**To run in the background**
```shell
docker-compose up -d
```

## Manual installation:
> **Make sure you have openjdk >= 11 installed on your machine.**   

> **Make sure you have PostgreSQL database installed and running on your machine.**  

> **You are going to need a database and a user with privileges to that database.**

**Clone the project**
```shell
git clone https://github.com/Asrez/WhereMoneyAPI.git
```

**Change directory**
```shell
cd WhereMoneyAPI
```

**Open up application.yml stored in `./src/main/resources/` file to set variables**
```shell
vi ./src/main/resources/application.yml
```

**Simply run the jar file with `--spring.config.location` specified**
```shell
java -jar jars/where-money-api.jar --spring.config.location=./src/main/resources/application.yml
```
