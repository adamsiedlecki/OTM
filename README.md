# OTM
Orchard Temperature Monitor is a tool for fruit growers that allows to check and monitor temperatures in distant places within orchard. During ground frosts, temperatures may vary a lot and it is important to have comprehensive information in order to implement adequate treatment (sich as spraying aspirin or other substances).

This application is just a part of a larger system, which also includes server on ESP8266 that communicate with Arduino stations in the orchard. It is responsible of fetching data from ESP server, storing it in database, processing and publishing it on a Facebook page when certain conditions are met.

<a>https://www.facebook.com/OrchardTemperatureMonitor/</a>

<h3>Technologies used:</h3>

- Java 11
- Spring Boot 2.4.4
- Spring Social
- JFreeChart
- some Thymeleaf
- MySQL
- JPA/Hibernate
- TestContainers 1.16.3
- Spock

<h3>Examples of charts:</h3>
<p align="center">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/chart1.jpg?raw=true"/>
</p>
<p align="center">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/long term chart.jpg"/>
</p>

<h3>Architecture overview:</h3>
<p align="center">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/otm-architecture-miro.jpg" height="70%" width="70%" align="center"/>
</p>

<h3>Station overview:</h3>
<p align="center">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/station overview.jpg" height="70%" width="70%" align="center"/>
</p>

<h3>Solar panels and station inside: </h3>
<div width="100%">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/big solar.jpg" height="30%" width="30%" align="left"/> <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/small solar.jpg" height="30%" width="30%"  align="center"/> <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/station inside.jpg" height="30%" width="30%" align="right"/>
</div>

<p align="center">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/station in blooming orchard.jpg" height="80%" width="80%" />
</p>

<h3>Facebook:</h3>
<p align="center">
  <img align="center" src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/frost facebook post.jpg" />
</p>

<p align="center">
  <img src="https://github.com/adamsiedlecki/readme-pics/blob/master/pics/otm/facebook page.jpg"/>
</p>
