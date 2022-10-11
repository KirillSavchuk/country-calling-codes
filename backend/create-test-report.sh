#For yet unknown reason 'surefire-report' does not generate '/css' and '/image' folders.
#So that it is required to perform such workaround to generate HTML report.
mvn clean test
mvn surefire-report:failsafe-report-only site -DgenerateReports=false
mvn surefire-report:report-only

#TODO: try to generate HTML report with Allure:
# > https://github.com/fescobar/allure-docker-service
# > https://github.com/fescobar/allure-docker-service-examples/tree/master/allure-docker-java-testng-example