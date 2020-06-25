## Получение данных с сервера, с помощью Retrofit. А также, хранение данных в базе данных[Sqlite], использование RxJava/RxAndroid и загрузка изображения Picasso.
## Обобщенно: Retrofit + RxJava/RxAndroid + Sqlite + Picasso + ListView + Fragment + Singleton[Design Patterns].

### Данные по супергероям/злодеям будут получены по API: https://www.superheroapi.com/api.php/894384864360671/search/batman
### Документация по API: https://superheroapi.com/index.html

### Подробнее.

### 1] Главный экран, на котором отображены имена супергероев и их изображения, взятые из базы данных. Список прокручиваемый. Нужно ввести имя супергероя, данные которого мы хотим просмотреть, чтобы сделать запрос: https://www.superheroapi.com/api.php/894384864360671/search/имя_супергероя
![project_superhero_main_ver'](https://user-images.githubusercontent.com/15383481/85705841-aaac9780-b6f2-11ea-8eb4-ac7e897147f6.png)
![project_superhero_main_hor'](https://user-images.githubusercontent.com/15383481/85705857-ad0ef180-b6f2-11ea-9804-cf7ab283b325.png)
### Удалить супергероя можно продолжительным нажатием на самого супергероя. Будет выведено диалоговое окно о подтверждении удаления. Супергерой будет удален из базы данных и из списка. 
### Можно очистить весь список супергеров, удалив их из базы данных.
### Для получения подробной информации о супергерое - нужно выбрать его из списка.

### 2] Экран, на котором можно получить подробную информацию о выбранном супергерое. Выводится вся доступная информация.
![project_superhero_info_ver'](https://user-images.githubusercontent.com/15383481/85706228-0b3bd480-b6f3-11ea-9c42-15b02b155586.png)
![project_superhero_info_hor'](https://user-images.githubusercontent.com/15383481/85706241-0d9e2e80-b6f3-11ea-933b-09189099a9b7.png)
### Можно сразу удалить супергероя. Супергерой будет удален из базы данных и из списка. Будет выведено диалоговое окно о подтверждении удаления.
### Можно обновить информацию о супергерое, перейдя на экран с "редактором".

### 3] Экран, на котором можно обновить информацию о супергерое. Будет сделан запрос в базу данных для обновления информации. 
![project_superhero_delete_ver'](https://user-images.githubusercontent.com/15383481/85706393-34f4fb80-b6f3-11ea-8e3f-51e3d46afa5c.png)
![project_superhero_delete_hor'](https://user-images.githubusercontent.com/15383481/85706405-37575580-b6f3-11ea-8378-16f2d49d82a1.png)

### Примечание:

#### 1] Это лишь пример использования Retrofit/RxJava/RxAndroid/Sqlite/Picasso/прочее в одной связке друг с другом. 
#### 2] Почти все окна содержат фрагменты.
#### 3] Для облегчения подстройки под разные разрешения экрана, были использованы единицы sdp/ssp.

#### sdp: https://github.com/intuit/sdp
#### ssp: https://github.com/intuit/ssp 

### Device: Samsung galaxy note 5.
