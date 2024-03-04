--GENRE
create table if not exists genre
(
    id   INTEGER auto_increment,
    name varchar(255) not null,
    constraint genre_catalog_pk
        primary key (id)
);

comment on table genre is 'Таблица с каталожных значений жанров';


--- RATING
create table if not exists rating
(
    id   INTEGER auto_increment,
    name varchar(10) not null,
    constraint rating_pk
        primary key (id)
);

comment on table rating is 'Таблица с каталожными значениями рейтинга MPA';

--- FILM
create table if not exists film
(
    id           int auto_increment
        primary key,
    name         varchar(255) not null,
    description  varchar(200) not null,
    release_date timestamp    not null,
    duration     int,
    rating       int,
    constraint FILM_RATING_ID_FK
        foreign key (RATING) references RATING

);

comment on table film is 'Таблица с данными о фильмах';

--- FILM_GENRE
create table if not exists film_genre
(
    film_id  int not null,
    genre_id int not null,
    constraint FILM_GENRE_FILM_ID_FK
        foreign key (film_id) references FILM ON DELETE CASCADE,
    constraint FILM_GENRE_GENRE_ID_FK
        foreign key (genre_id) references GENRE ON DELETE CASCADE
);

comment on table film_genre is 'Таблица развязки фильмов и их жанров';

---USERS
create table if not exists users
(
    id       int auto_increment
        primary key,
    email    varchar(255) not null,
    login    varchar(255) not null,
    name     varchar(255),
    birthday date         not null
);

comment on table users is 'Таблица с данными о пользователях';


---FAVORITE_FILMS
create table if not exists favorite_films
(
    user_id int not null,
    film_id int not null,
    constraint FAVORITE_FILMS_FILM_ID_FK
        foreign key (film_id) references FILM ON DELETE CASCADE,
    constraint FAVORITE_FILMS_USERS_ID_FK
        foreign key (user_id) references USERS ON DELETE CASCADE
);

comment on table favorite_films is 'Таблица развязки по избранным фильмам пользователя';

---FRIENDSHIP
create table if not exists friendship
(
    user_from int     not null,
    user_to   int     not null,
    approved  boolean not null default false,
    constraint FRIENDSHIP_USERS_ID_FK
        foreign key (user_from) references USERS ON DELETE CASCADE,
    constraint FRIENDSHIP_USERS_ID_FK_2
        foreign key (user_to) references USERS ON DELETE CASCADE
);

comment on table friendship is 'Таблица дружбы между пользователями';

--REVIEW
create table if not exists reviews
(
    review_id INTEGER auto_increment,
    content   varchar not null,
    positive  boolean not null,
    user_Id   INTEGER not null,
    film_Id   INTEGER not null,
    useful    INTEGER not null default  0,
    constraint reviews_pk
        primary key (review_id),
    constraint reviews_FILM_ID_fk
        foreign key (film_Id) references FILM ON DELETE CASCADE,
    constraint reviews_USERS_ID_fk
        foreign key (user_Id) references USERS ON DELETE CASCADE
);

comment on table reviews is 'Таблица с отзывами о фильмах';

create table if not exists review_likes
(
    review_Id INTEGER not null,
    user_id   INTEGER not null,
    is_liked  boolean not null,
    constraint review_likes_REVIEWS_REVIEW_ID_fk
        foreign key (review_Id) references REVIEWS ON DELETE CASCADE,
    constraint review_likes_USERS_ID_fk
        foreign key (user_id) references USERS ON DELETE CASCADE
);

comment on table review_likes is 'Таблица для оценки отзывов пользователями';
