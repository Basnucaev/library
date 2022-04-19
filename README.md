# library
 
 1. BookController - реализованы все crud операции с помощью энд поинтов с нужным request методом, операции происходят с json книги, которая записывается в базу данных и сохраняет файл закрепленный за книгой в локальную директорию на компьютере в методе addBook()
 2. FileBookController - с помощью этого контроллера можно загружать и выгружать файлы книг с локального сервера, есть возможность добавить дополнительные файлы к уже существующей книге в базе данных. Файлы хранятся на локальном диске, в базе данных сохранена информация о файле (путь, размер, имя файла и uri для каждого файла
 3. LibraryLogicController - через этот контроллер реализована логика библиотеки, поиск книг по названию, автору и ценовому диапазону, все методы возвращают лист книг

В сервисах реализована вся бизнес логика приложения. Созданы свои исключения, если проверки написанные в сервисах не проходят, выбрасывается соответсвующее исключение и ловится Global Handler классом.

Написаны тесты для всех контроллеров, сервисов и ручных запросов сделанных при помощи аннотации @Query в репозитории, покрытие тестами более 70%:

