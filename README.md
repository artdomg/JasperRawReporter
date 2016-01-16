# JasperRawReporter

## Usage

* `java -jar JasperRawReporter.jar ReportPath.jasper format parametersJSON databaseCredentialsJSON`

## Example

* `java -jar JasperRawReporter.jar /home/myuser/myreport.jasper html '{"param1": "myparamvalue"}' '{"vendor": "mysql", "host": "127.0.0.1", "db": "mydatabase", "user": "username", "password": "mysecurepassword"}'`

## Currently supported vendors

* mysql
