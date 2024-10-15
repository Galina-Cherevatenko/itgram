--liquibase formatted sql

--changeset cherevatenko:1 labels:v0.0.1
CREATE TYPE "publication_category_type" AS ENUM ('post', 'start', 'ad');
CREATE TYPE "publication_visibilities_type" AS ENUM ('public', 'owner', 'group');

CREATE TABLE "publications" (
	"id" text primary key constraint publications_id_length_ctr check (length("id") < 64),
	"title" text constraint publications_title_length_ctr check (length(title) < 128),
	"description" text constraint publications_description_length_ctr check (length(title) < 4096),
	"publication_category" publication_category_type not null,
	"visibility" publication_visibilities_type not null,
	"owner_id" text not null constraint publications_owner_id_length_ctr check (length(id) < 64),
	"lock" text not null constraint ads_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX publications_owner_id_idx on "publications" using hash ("owner_id");

CREATE INDEX publications_visibility_idx on "publications" using hash ("visibility");
