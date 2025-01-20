# Aimodosia Backend
This is the backend of the web app Aimodosia in Java/SpringBoot.


## Docker Setup and run
#### (run the commands in the terminal to run docker for windows)

```sh
docker run --name ds-lab-pg --rm `
-e POSTGRES_PASSWORD=pass123 `
-e POSTGRES_USER=dbuser `
-e POSTGRES_DB=aimodosia `
-d -p 5432:5432 -v ds-lab-vol:/var/lib/postgresql/data `
postgres:14
```

### Admin's details for login are:
#### username: admin
#### password: 1234