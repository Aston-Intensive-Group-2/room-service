CREATE TABLE "rooms"
(
    "room_id" bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "room_width" integer NOT NULL,
    "room_length" integer NOT NULL,
    "room_height" integer NOT NULL,
    "room_address" varchar(256) NOT NULL,
    "room_status" varchar(32) NOT NULL
);

CREATE TABLE "equipments"
(
    "equipment_id" bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "equipment_name"  varchar(128) NOT NULL,
    "equipment_type"  varchar(64) NOT NULL,
    "rooms_room_id" bigint REFERENCES "rooms" (room_id)
);
