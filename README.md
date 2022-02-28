# WhereMoney Webservice

## Installation guid
The easiest way to install the webservice is by using Docker.

## Docker installation:
**Clone the project**
```shell
git clone https://github.com/Asrez/WhereMoneyAPI.git
```
**Open up .env file to set env variables**
```shell
vi ./.env
```
![.env file.](https://i.postimg.cc/VL8mmJKJ/Screenshot-from-2022-02-28-21-37-03.png)
> :warning: **Make sure to change sensitive data in .env file or your data might be as risk...**
**Change directory**
```shell
cd WhereMoneyAPI
```

**docker-composer**
```shell
docker-compose up
```
**To run in the background**
```shell
docker-compose up -d
```
