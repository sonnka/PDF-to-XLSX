# PDF-to-XLSX
PDF-to-XLSX - a RESTful application that reads text from a PDF file, exctracts information based on certain list of keywords and saves it in an Excel file.

You need to transfer the pdf file as a parameter to the endpoint:

    POST /extractTextFromPdf
    
## Technologies

The following technologies are used:
- Java 17
- Spring Boot  3.1.1
-	Google Cloud Vision API 4.5.1
-	Apache POI  5.2.3
-	Stanford Core NLP  4.5.4
-	Lombok 1.18.28
