mkdir $(pwd)/target/jetty
mkdir $(pwd)/target/jetty/SReport
pwd;
cp $(pwd)/target/SReport.war $(pwd)/target/jetty/SReport
cd $(pwd)/target/jetty/SReport;
jar -xvf SReport.war