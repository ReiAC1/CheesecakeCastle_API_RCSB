--we should create an ID number for users?
--what should we do with this is_admin

create table users(
    username varchar(20), --pkey
    fname varchar(20),
    lname varchar (40),
    password varchar (50),
    dob date,
    is_admin varchar(3)
    );

--should not be able to change order date
create table users_payment(
    payment_id int, --pkey
    balance double,
    exp_date date,
    ccv int,
    zip_code int,
    provider varchar(50),
    customer_username (varchar)
    );

create table dish(
    dish_id int, --pkey
    dish_name varchar(30),
    cost double,
    description varchar(100),
    is_vegetarian varchar (3)
);

create table orders(
    order_id int, --pkey
    amount double,
    order_date date,
    order_address varchar(50),
    order_zip int, --possibly limit the number of zip codes
    customer_username varchar (20), --fkey
    payment_id int
    );

create table order_details(
    order_detail_id int, --pkey
    dish_id int, -- fkey
    order_id int, --fkey
    quantity int,
    comments varchar(50)
);

alter table users add primary key (username);
alter table users_payment add primary key (payment_id);
alter table dish add primary key (dish_id);
alter table orders add primary key (order_id);
alter table orders add constraint fk_customer_username foreign key (customer_username)references users(username);
alter table order_details add constraint fk_dish_id foreign key (dish_id) references dish(dish_id);
alter table order_details add constraint fk_order_id foreign key (order_id) references orders(order_id);





