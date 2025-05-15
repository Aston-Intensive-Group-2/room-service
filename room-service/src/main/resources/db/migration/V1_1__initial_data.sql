INSERT INTO "rooms" (room_width, room_length, room_height, room_address, room_status)
VALUES (3, 4, 3, '1600 Amphitheatre Parkway, Mountain View, CA 94043', 'UNLOCKED'),
            (3, 4, 3, '1600 Amphitheatre Parkway, Mountain View, CA 94043', 'LOCKED'),
            (3, 4, 3, '1600 Amphitheatre Parkway, Mountain View, CA 94043', 'UNLOCKED'),
            (3, 4, 3, '1600 Amphitheatre Parkway, Mountain View, CA 94043', 'UNLOCKED'),
            (3, 4, 3, 'One Microsoft Way, Redmond, WA 98052-8300', 'UNLOCKED'),
            (4, 5, 3, 'One Microsoft Way, Redmond, WA 98052-8300', 'UNLOCKED'),
            (4, 5, 3, 'One Microsoft Way, Redmond, WA 98052-8300', 'LOCKED'),
            (4, 5, 3, 'One Microsoft Way, Redmond, WA 98052-8300', 'UNLOCKED'),
            (4, 5, 3, '410 Terry Avenue North, Seattle, WA 98109', 'UNLOCKED'),
            (4, 5, 3, '410 Terry Avenue North, Seattle, WA 98109', 'UNLOCKED');

INSERT INTO "equipments" (equipment_name, equipment_type, rooms_room_id)
VALUES ('Printer HP', 'PRINTER', 1),
            ('Scanner HP', 'SCANNER', 1),
            ('Conditioner Electrolux', 'CONDITIONER', 1),
            ('Projector LG', 'PROJECTOR', 1),
            ('Projector LG', 'PROJECTOR', 2),
            ('Conditioner Electrolux', 'CONDITIONER', 2),
            ('Printer HP', 'PRINTER', 7),
            ('Scanner HP', 'SCANNER', 7),
            ('Conditioner Electrolux', 'CONDITIONER', 7),
            ('Projector LG', 'PROJECTOR', 7);
