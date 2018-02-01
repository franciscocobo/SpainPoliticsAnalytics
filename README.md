# DUIme
Proyecto para la asignatura optativa de Computación Distribuida.

## Getting Started

La idea del proyecto es analizar el posicionamiento de los partidos políticos españoles en las redes sociales Facebook y YouTube.

## Facebook

### Fuentes

  * [Analasis Lenguaje Natural Castellano en Python](http://www.bluefiredev.com/2017/03/29/mc-analisis-de-sentimiento-en-espanol-con-python-introduccion/) - Análisis del lenguaje natura en base a un diccionario de palabras ponderadas.
  * [Python Facebook Scrapper](https://nocodewebscraping.com/facebook-scraper/) - Cómo extraer Posts y Comentarios de publicaciones de Facebook a Python.
  * [API Facebook](https://developers.facebook.com/tools/explorer/) - En particular hemos hecho uso de la *API Graph* para aprender como extraer datos de Facebook.

### Desarrollo

  * El primer paso es obtener los datos con los que vamos a trabajar, esto lo hacemos con el código incluido en `scrapingFacebookPython`.
  1) Primero obtenemos los posts en un archivo *.csv* (los podemos encontrar en `CSVfiles`). Esto lo hacemos en `posts.py`. De aquí obtemos unas figuras que se pueden encontrar en `PandasScrapping.ipynb`.
  2) Después obtenemos de estos posts los comentarios para los 7000 primeros (en nuestro caso). Estos archivos, también *.csc* los podemos encontrar en `CSVfiles`.

  * El segundo paso ha sido analizar los sentimientos encontrados en los comentarios de Facebook de los posts y hacer histogramas con los resultados. El código lo podemos encontrar en `analisisLenguajeNatural`, particularmente el resultado final de hacer histogramas con los resultados por cada partido político, en `scriptAnalizador.py`. Los resultados de sentimientos se mueven entre 1 (más negativo) y 2 (más positivo). Todos los partidos políticos reciben unos resultados similares debido a la inexactitud de nuestro modelo de análisis. Las imagenes obtenidas se encuentran en `imagenesObtenidas`. Encontramos tres Python scripts en el análisis del lenguaje natural:
1) En `entrenador.py` generamos el archivo binario para evitar generar el clasificador una y otra vez, después de generarlo a partir de nuestro diccionario de palabras ponderadas. 
2) En `es_nlp.py` hacemos una extracción de palabras que luego llamaremos desde otros scripts.
3) Finalmente, en `scriptFinalAnalizador.py`, a partir de los datos extraidos de los comentarios de Facebook hacemos un histograma con los valores de sentimientos que desprenden.

 Resultaría interesante utilizar un analizador de sentimientos más potente y un poder de cómputo mayor para poder analizar estos sentimientos respectos a los posts de una empresa en Facebook, para poder reaccionar frente a ello.
 
 ### Resultados obtenidos
 
 ![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/Imagen1.png)
 ![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/Imagen2.png)
 
 **Formato de un .csv obrenido de un post/publicación**
 ![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/Imagen3.png)
 ![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/Imagen4.png)
 
 **Formato de un .csv obrenido de los comentarios de un post/publicación**
 ![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/Imagen5.png)
 
 
**PP**

Número de posts analizados: 5564 Statuses Processed in 0:05:12.025780
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/ppPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosPP.png)

**PSOE**

Número de posts analizados: 5256 Statuses Processed in 0:05:26.555868
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/PSOEPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosPSOE.png)

**PODEMOS**

Número de posts analizados: 2529 Statuses Processed in 0:04:55.482784
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/PodemosPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosPODEMOS.png)

**Cs**

Número de posts analizados: 5256 Statuses Processed in 0:05:06.046433
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/CsPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosCs.png)

**JxS**

Número de posts analizados: 626 Statuses Processed in 0:00:55.359729
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/JxSPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosJxS.png)

**ERC**

Número de posts analizados: 4983 Statuses Processed in 0:05:01.012039
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/ERCPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosERC.png)

**CUP**

Número de posts analizados: 3600 Statuses Processed in 0:03:41.029068
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/CUPPosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosCUP.png)

**Vox**

Número de posts analizados: 2553 Statuses Processed in 0:04:30.121073
![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/VOXosts.png)
*Análisis Sentimientos*

![alt text](https://github.com/psanch21/DUIme/blob/master/%23Facebook/imagenesObtenidas/sentimientosVox.png)


## Youtube

### Fuentes

 * [Youtube API v3](https://developers.google.com/youtube/v3/) - La API está muy bien documentada y cuenta con guías y código de ejemplo que facilita mucho su uso.

 * [Google Auth2](https://developers.google.com/identity/protocols/OAuth2) - Nos permite obtener los tokens para acceder a la API

### Desarrollo

 * `Credenciales:` Antes de poder acceder a la API, tenemos que conseguir las credenciales correspondientes al nivel de acceso que necesitamos. En nuestro caso únicamente de lectura
 
 * `Búsquedas:` Realizamos las busquedas correspondientes que nos permiten obtener la información de los canales deseados (partidos políticos y periódicos españoles).
 
 * `Análisis:` Realizamos un análisis descriptivo de los datos, obteniendo datos estadísticos que pueden ser de utilidad ( historgramas, medias, evolución temporal...). Para realizar esta tarea hemos utilizado la herramienta `Jupyter Notebook`.
 

## Authors

  * **Pablo Sanchez** - *Estudiante UC3M* - [psanch21](https://github.com/psanch21)
  * **Francisco Cobo** - *Estudiante UC3M* - [franciscocobo](https://github.com/franciscocobo)

