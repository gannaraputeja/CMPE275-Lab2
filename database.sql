

create table if not exists Passenger(
	id varchar(255) primary key,
	firstname varchar(255),
	lastname varchar(255),
	birthyear int,
	gender varchar(255),
	phone varchar(255) unique
);

create table if not exists Reservation(
	reservation_number varchar(255) primary key,
	passenger_id varchar(255),
	origin varchar(255),
	destination varchar(255),
	price int
);

create table if not exists Flight(
	flight_number varchar(255),
	departure_date date,
	departure_time datetime,
	arrival_time datetime,
	price int,
	origin varchar(255),
	destination varchar(255),
	seats_left int,
	description varchar(255),
	model varchar(255),
	capacity int,
	manufacturer varchar(255),
	year_of_manufacture int,
	primary key (flight_number, departure_date)
);

create table if not exists Reservation_Flight(
	reservation_number varchar(255),
	flight_number varchar(200),
	departure_date date,
	constraint fk_r_f_1 foreign key (reservation_number) references Reservation(reservation_number),
	constraint fk_r_f_2 foreign key (flight_number, departure_date) references Flight(flight_number, departure_date)
);

create table if not exists Flight_Passenger(
	flight_number varchar(255),
	departure_date date,
	passenger_id varchar(255),
	constraint fk_f_p_1 foreign key (flight_number, departure_date) references Flight(flight_number, departure_date),
	constraint fk_f_p_2 foreign key (passenger_id) references Passenger(id)
);

desc Passenger;
desc Reservation;
desc Flight;
desc Reservation_Flight;
desc Flight_Passenger;


-- drop table Flight_Passenger;
-- drop table Reservation_Flight;
-- drop table Flight;
-- drop table Reservation;
-- drop table Passenger;

show tables;





