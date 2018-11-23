INSERT INTO account (id, username, email, password, notification_interval_in_days, is_admin, created_at, notified_at) VALUES
    (2, 'patrik', 'patrik@gmail.com', '$2a$10$1.i7NoM1f6ihuXNbPSOHsOmwHq5xfAwwA58L.dWNdyzKcEQ1.pR4K', 1, TRUE, NOW(), NOW()),
    (3, 'alexia', 'alexia@gmail.com', '$2a$10$sWkn7hYwns1cEqd376PIUOk6.xMVld5hwf2OPYaNsxJ4XCjM.KPl.', 9, FALSE, NOW(), NOW()),
    (6, 'kristoffer', 'kristoffer@gmail.com', '$2a$10$aGgHC9mmV9CX/jZNORhlXexQRTtgBh4.feis2sxfeLqKthko8SlT6', 30, FALSE, NOW(), NOW());

INSERT INTO subscription (id, remote_id, name, overview, image_url, vote_count, vote_average, first_air_date, last_air_date, homepage, number_of_episodes, number_of_seasons, created_at, updated_at, last_update, season_last_update, next_air_date, next_air_date_is_new_season) VALUES
    (2, 2, 'breaking bad', 'Meth dealing tutorial', 'image', 29, 9.1, '2010-01-01', '2014-06-01', 'www.breakingbad.com', 55, 6, NOW(), NOW(), null, 6, null, null),
    (3, 3, 'line of duty', 'Corrupt police investigations', 'image', 6, 7.5, '2009-05-18', '2017-02-28', 'www.lineofduty.com', 48, 5, NOW(), NOW(), null, 5, null, null),
    (6, 1, 'game of thrones', 'Winter is coming...', 'image', 10, 8.6, '2013-05-15', '2018-05-16', 'www.got.com', 72, 8, NOW(), NOW(), null, 8, null, null);

INSERT INTO account_subscription (account_id, subscription_id) VALUES
    (2, 2),
    (2, 3),
    (6, 3),
    (6, 6);

INSERT INTO subscription_entity_genres (subscription_entity_id, genres) VALUES
    (2, 'Thriller'),
    (2, 'Drama'),
    (3, 'Crime'),
    (3, 'Drama'),
    (6, 'Fantasy'),
    (6, 'Drama');
