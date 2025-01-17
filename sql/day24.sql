create database day24_ws;
use day24_ws;

-- Day 24 in class demo - use command line client to demonstrate 2 sessions (isolation levels)
create table products (
	product_id int primary key auto_increment,
    product_name varchar(50),
    quantity int
);

desc products;

insert into products (product_name, quantity) values ('Widget', 100);
insert into products (product_name, quantity) values ('Gadget', 200);

select * from products;


-- Day 24 in class workshop
create table purchase_order (
    order_id char(8) not null,
    name varchar(120) not null,
    order_date date not null,
    
    primary key (order_id)
);

create table line_item (
    item_id int auto_increment not null,
    description text not null,
    quantity int default '1',
    order_id char(8) not null,

    primary key (item_id),
    constraint fk_order_id 
        foreign key (order_id) references purchase_order (order_id)
);

-- Day 24 workshop (pdf)
create table orders (
	order_id int(10) not null auto_increment,
    order_date date,
    customer_name varchar(128),
    ship_address varchar(128),
    notes text,
    tax decimal(2,2) default '0.05',
    constraint pk_order_id primary key(order_id)
);

create table order_details (
	id int(10) not null auto_increment,
    product varchar(64),
    unit_price decimal(3,2),
    discount decimal(3,2) default '1.0',
    quantity int,
    order_id int(10) not null,
    constraint pk_id primary key(id),
    constraint fk_orders_id foreign key(order_id)
		references orders(order_id)
);