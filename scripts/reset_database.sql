DROP TABLE IF EXISTS general_config cascade;
DROP TABLE IF EXISTS teams cascade;
DROP TABLE IF EXISTS players cascade;
DROP TABLE IF EXISTS blocks cascade;
DROP TABLE IF EXISTS start_inventory cascade;

create table general_config(
    key varchar(64) constraint primary_key_general_config primary key,
    value integer
);

CREATE TABLE teams(
    id SERIAL CONSTRAINT primary_key_teams PRIMARY KEY,
    name varchar(64) not null,
    score integer not null
);

CREATE TABLE players(
    uuid varchar(64) CONSTRAINT primary_key_players PRIMARY KEY,
    name varchar(64) not null,
    team integer,
    isHost boolean not null,
    FOREIGN KEY (team) references teams(id)
);

create table blocks(
    id SERIAL CONSTRAINT primary_key_blocks PRIMARY KEY,
    material varchar(64) unique not null,
    points integer not null
);

create table start_inventory(
    key integer constraint primary_key_start_inventory primary key,
    material varchar(64),
    count integer
);

insert into teams values(default, 'Bleu', 0);
insert into teams values(default, 'Rouge', 0);
insert into teams values(default, 'Vert', 0);
insert into teams values(default, 'Jaune', 0);
insert into teams values(default, 'Orange', 0);
insert into teams values(default, 'Rose', 0);
insert into teams values(default, 'Noir', 0);
insert into teams values(default, 'Gris', 0);
insert into teams values(default, 'Cyan', 0);

insert into general_config values('numberTeams', 2);
insert into general_config values('teamsFreezed', 0);
insert into general_config values('previousNumberTeams', 2);
insert into general_config values('previousNumberItems', 0);
insert into general_config values('gamePhase', 0);