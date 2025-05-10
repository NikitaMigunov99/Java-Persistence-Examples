INSERT INTO author (id, name)
VALUES
(NEXTVAL('author_seq'), 'John Doe'),
(NEXTVAL('author_seq'), 'Jane Smith'),
(NEXTVAL('author_seq'), 'Michael Johnson'),
(NEXTVAL('author_seq'), 'Emily Davis'),
(NEXTVAL('author_seq'), 'Christopher Wilson');


INSERT INTO joke (id, type, setup, punchline, author_id)
VALUES
(NEXTVAL('joke_seq'), 'general', 'Why did the burglar hang his mugshot on the wall?', 'To prove that he was framed!', 1),
(NEXTVAL('joke_seq'), 'general', 'What do you call a monkey in a mine field?', 'A babooooom!', 2),
(NEXTVAL('joke_seq'), 'general', 'What did the Dorito farmer say to the other Dorito farmer?', 'Cool Ranch!', 1),
(NEXTVAL('joke_seq'), 'general', 'Why did the girl smear peanut butter on the road?', 'To go with the traffic jam.', 1),
(NEXTVAL('joke_seq'), 'programming', 'Lady: How do I spread love in this cruel world?', 'Random Dude:', 2),
(NEXTVAL('joke_seq'), 'programming', 'What is the best thing about a Boolean?', 'Even if you are wrong, you are only off by a bit.', 3),
(NEXTVAL('joke_seq'), 'general', 'What type of music do balloons hate?', 'Pop music!', 4),
(NEXTVAL('joke_seq'), 'programming', 'Why do not programmers like nature?', 'Too many bugs.', 5),
(NEXTVAL('joke_seq'), 'general', 'What do you get when you cross a snowman with a vampire?', 'Frostbite.', 3),
(NEXTVAL('joke_seq'), 'general', 'Why did the fireman wear red, white, and blue suspenders?', 'To hold his pants up.', 1);

INSERT INTO address (id, city, author_id) VALUES
(1, 'City 1', 1),
(2, 'City 2', 2),
(3, 'City 3', 3);



