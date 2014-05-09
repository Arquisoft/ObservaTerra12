ObservaTerra1.2
=============

Estado: _EN DESARROLLO_.

Portal web que actúa como observatorio de datos de la tierra. 

ObservaTerra es un punto de unión entre la información obtenida por diferentes organizaciones que permite comparar y contrastar datos de cada país. El portal ofrece el lugar de encuentro perfecto para los interesados en estos datos: periodistas, políticos, estadísticos, personal de ONGs, etc.

La aplicacion cuenta con tres modulos independientes que deben ser iniciados por separado.

* La base de datos se inicia con el script startup.bat que se puede encontrar en ObservaTerra12/public/BaseDatosUsuarios
* La aplicacion web se inicia como una aplicacion Play normal
* Para el funcionamiento de la demo no es necesario el crawler pero se puede iniciar desde su clase Crawler.java

Para utilizar la demo puede registrarse o utilizar uno de los usuarios ya creados como:

* Usuario: periodista1
* Contraseña: periodista1

Compilacion y pruebas del proyecto:

* Para compilar el proyecto es necesario hacer una compilacion con Maven y una compilacion con Play
* Las pruebas es posible ejecutarlas en local o bien dejar que Travis se encargue de ello (este proyecto de GitHub ya esta enlazado con Travis)


Desarrolladores:
* Jose Enrique Álvarez Rendal
* Ignacio Astorga Coto
* Manuel Casas Toscano
* Pablo García Fernández
* Alejandro Pato Tellada


