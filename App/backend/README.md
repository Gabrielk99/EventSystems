# Rotas

## Gestores

**Pegar as informaçes básicas de um gestor:**

```
GET http://localhost/6001/api/gestor/
```
Exemplo de retorno:
```json
{
  "gestores":[
        {
            "name":"Patricia",
            "id":0,
            "email":"gerentepatriciaa@gmail.com",
            "celular":"+55 27 9999-9999",
            "img":"patricia.jpeg"
        },
        {
            "name":"Mikaella",
            "id":1,
            "email":"mikaellagerentea@gmail.com",
            "celular":"+55 27 8888-8888",
            "img":"mikagay.jpeg"
        },
        {
            "name":"Geraldo",
            "id":2,
            "email":"geraldogerentea@gmail.com",
            "celular":"+55 27 7777-7777",
            "img":"geraldo.jpeg"
        }
        
        
    ]
}
```

**Pegar as informaçes básicas de um gestor específico**
```
GET http://localhost/6001/api/gestor/<id>
```

Exemplo de retorno `<id> = 2`:
```json
{
  "name":"Geraldo",
  "id":2,
  "email":"geraldogerentea@gmail.com",
  "celular":"+55 27 7777-7777",
  "img":"geraldo.jpeg"
}
```


**Pegar a localização atual dos gestores**
```
GET http://localhost/6001/api/gestor/localizacao
```
Exemplo de retorno
```json
[
  {
    "id": 0,
    "dataSaved": {
      "location": {
        "latitude": -40.30404,
        "longitude": -20.28792
      }
    }
  },
  {
    "id": 1,
    "dataSaved": {
      "location": {
        "latitude": -40.30312,
        "longitude": -20.28403
      }
    }
  },
  {
    "id": 2,
    "dataSaved": {
      "location": {
        "latitude": -40.313,
        "longitude": -20.29511
      }
    }
  }
]
```

**Pegar as informaçes básicas de um gestor específico**
```
GET http://localhost/6001/api/gestor/localizacao/<id>
```

Exemplo de retorno `<id> = 2`:
```json
{
  "id": 2,
    "dataSaved": {
      "location": {
        "latitude": -40.313,
        "longitude": -20.29511
      }
    }
}
```
## Vacinas


**Pegar as informaçes básicas de uma vacina:**

```
GET http://localhost/6001/api/vacina/
```
Exemplo de retorno:
```json
{
  "vacinas":[
    {
      "name":"Pfizer",
      "id":0
    },
    {
      "name":"Janssen",
      "id":3
    },
    {
      "name":"Coronavac",
      "id":2
    },
    {
      "name":"Astrazeneca",
      "id":1
    }    
  ]
}
```

**Pegar as informaçes básicas de uma vacina específica**
```
GET http://localhost/6001/api/vacina/<id>
```

Exemplo de retorno `<id> = 0`:
```json
{
  "name":"Pfizer",
  "id":0
}
```


**Pegar os últimos status de todas as vacinas**
```
GET http://localhost/6001/api/vacina/status
```
Exemplo de retorno
```json
[
  {
    "size": 2,
    "id": 0,
    "datasSaved": [
      {
        "status": 0,
        "temperature": "-75.5",
        "date": "Thu Jan 20 23:58:37 BRT 2022",
        "location": {
          "latitude": -40.29453,
          "longitude": -20.28239
        }
      },
      {
        "status": 1,
        "temperature": "-75.0",
        "date": "Thu Jan 20 23:58:39 BRT 2022",
        "location": {
          "latitude": -40.29483,
          "longitude": -20.28234
        }
      }
    ]
  },
  {
    "size": 2,
    "id": 0,
    "datasSaved": [
      {
        "status": 0,
        "temperature": "-75.5",
        "date": "Thu Jan 20 23:58:37 BRT 2022",
        "location": {
          "latitude": -40.29453,
          "longitude": -20.28239
        }
      },
      {
        "status": 1,
        "temperature": "-75.0",
        "date": "Thu Jan 20 23:58:39 BRT 2022",
        "location": {
          "latitude": -40.29483,
          "longitude": -20.28234
        }
      }
    ]
  }
]
```

**Pegar os últimos status de uma vacina específica**
```
GET http://localhost/6001/api/vacina/status/<id>
```

Exemplo de retorno `<id> = 0`:
```json
{
    "size": 2,
    "id": 0,
    "datasSaved": [
      {
        "status": 0,
        "temperature": "-75.5",
        "date": "Thu Jan 20 23:58:37 BRT 2022",
        "location": {
          "latitude": -40.29453,
          "longitude": -20.28239
        }
      },
      {
        "status": 1,
        "temperature": "-75.0",
        "date": "Thu Jan 20 23:58:39 BRT 2022",
        "location": {
          "latitude": -40.29483,
          "longitude": -20.28234
        }
      }
    ]
  }
```
