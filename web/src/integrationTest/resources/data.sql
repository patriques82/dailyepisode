INSERT INTO account (id, username, email, password, notification_interval_in_days, is_admin, created_at, notified_at) VALUES
    (1, 'patrik', 'patriknygren82@gmail.com', '$2a$10$1.i7NoM1f6ihuXNbPSOHsOmwHq5xfAwwA58L.dWNdyzKcEQ1.pR4K', 1, 1, NOW(), NOW()),
    (2, 'alexia', 'alexia@gmail.com', '$2a$10$sWkn7hYwns1cEqd376PIUOk6.xMVld5hwf2OPYaNsxJ4XCjM.KPl.', 9, 0, NOW(), NOW()),
    (3, 'kristoffer', 'kristoffer@gmail.com', '$2a$10$aGgHC9mmV9CX/jZNORhlXexQRTtgBh4.feis2sxfeLqKthko8SlT6', 30, 0, NOW(), NOW());

INSERT INTO subscription (id, remote_id, name, overview, image_url, vote_count, vote_average, first_air_date, last_air_date, homepage, number_of_episodes, number_of_seasons, created_at, updated_at) VALUES
    (1, 2, 'breaking bad', 'Meth dealing tutorial', 'image', 29, 9.1, '2010-01-01', '2014-06-01', 'www.breakingbad.com', 55, 6, NOW(), NOW()),
    (2, 3, 'line of duty', 'Corrupt police investigations', 'image', 6, 7.5, '2009-05-18', '2017-02-29', 'www.lineofduty.com', 48, 5, NOW(), NOW()),
    (3, 1, 'game of thrones', 'Winter is coming...', 'image', 10, 8.6, '2013-05-15', '2018-05-16', 'www.got.com', 72, 8, NOW(), NOW());

INSERT INTO account_subscription (account_id, subscription_id) VALUES
    (1, 1),
    (1, 2),
    (3, 2),
    (3, 3);

INSERT INTO subscription_entity_genres (subscription_entity_id, genres) VALUES
    (1, 'Thriller'),
    (1, 'Drama'),
    (2, 'Crime'),
    (2, 'Drama'),
    (3, 'Fantasy'),
    (3, 'Drama');
