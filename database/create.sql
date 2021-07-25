CREATE TABLE "users"(
    "id" INTEGER NOT NULL,
    "nickname" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "first_name" VARCHAR(255) NOT NULL,
    "last_name" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "registration_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "last_modyfied_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "last_modyfied_author" INTEGER NOT NULL,
    "deletion_date" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "deletion_author" INTEGER NULL,
    "course_id" INTEGER NULL,
    "is_admin" BOOLEAN NOT NULL
);
CREATE INDEX "users_first_name_index" ON
    "users"("first_name");
CREATE INDEX "users_last_name_index" ON
    "users"("last_name");
CREATE INDEX "users_is_admin_index" ON
    "users"("is_admin");
ALTER TABLE
    "users" ADD PRIMARY KEY("id");
ALTER TABLE
    "users" ADD CONSTRAINT "users_email_unique" UNIQUE("email");
CREATE TABLE "courses"(
    "id" INTEGER NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "author" INTEGER NOT NULL,
    "description" TEXT NULL,
    "duration" TIME(0) WITHOUT TIME ZONE NULL,
    "creation_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "creation_author" INTEGER NOT NULL,
    "last_modyfied_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "last_modyfied_author" INTEGER NOT NULL,
    "deletion_date" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "deletion_author" INTEGER NULL,
    "rating" DOUBLE PRECISION NULL
);
CREATE INDEX "courses_title_index" ON
    "courses"("title");
CREATE INDEX "courses_author_index" ON
    "courses"("author");
CREATE INDEX "courses_duration_index" ON
    "courses"("duration");
CREATE INDEX "courses_rating_index" ON
    "courses"("rating");
ALTER TABLE
    "courses" ADD PRIMARY KEY("id");
CREATE TABLE "courses_users"(
    "course_id" INTEGER NOT NULL,
    "user_id" INTEGER NOT NULL
);
ALTER TABLE
    "courses_users" ADD PRIMARY KEY("course_id");
ALTER TABLE
    "courses_users" ADD PRIMARY KEY("user_id");
CREATE TABLE "modules"(
    "id" INTEGER NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "description" TEXT NULL,
    "creation_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "creation_author" INTEGER NOT NULL,
    "deletion_date" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "deletion_author" INTEGER NULL,
    "course_id" INTEGER NOT NULL
);
CREATE INDEX "modules_title_index" ON
    "modules"("title");
ALTER TABLE
    "modules" ADD PRIMARY KEY("id");
CREATE TABLE "themes"(
    "id" INTEGER NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "description" TEXT NULL,
    "creation_date" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "creation_author" INTEGER NOT NULL,
    "deletion_date" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "deletion_author" INTEGER NULL,
    "module_id" INTEGER NOT NULL
);
CREATE INDEX "themes_title_index" ON
    "themes"("title");
ALTER TABLE
    "themes" ADD PRIMARY KEY("id");
CREATE TABLE "modules_themes"(
    "module_id" INTEGER NOT NULL,
    "theme_id" INTEGER NOT NULL
);
ALTER TABLE
    "modules_themes" ADD PRIMARY KEY("module_id");
ALTER TABLE
    "modules_themes" ADD PRIMARY KEY("theme_id");
CREATE TABLE "courses_modules"(
    "course_id" INTEGER NOT NULL,
    "module_id" INTEGER NOT NULL
);
ALTER TABLE
    "courses_modules" ADD PRIMARY KEY("course_id");
ALTER TABLE
    "courses_modules" ADD PRIMARY KEY("module_id");
CREATE TABLE "courses_rating"(
    "course_id" INTEGER NOT NULL,
    "user_id" INTEGER NOT NULL,
    "mark" INTEGER NOT NULL
);
ALTER TABLE
    "courses_rating" ADD PRIMARY KEY("course_id");
ALTER TABLE
    "courses_rating" ADD PRIMARY KEY("user_id");
CREATE TABLE "categories"(
    "id" INTEGER NOT NULL,
    "title" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "categories" ADD PRIMARY KEY("id");
CREATE TABLE "courses_categories"(
    "course_id" INTEGER NOT NULL,
    "category_id" INTEGER NOT NULL
);
ALTER TABLE
    "courses_categories" ADD PRIMARY KEY("course_id");
ALTER TABLE
    "courses_categories" ADD PRIMARY KEY("category_id");
CREATE TABLE "courses_tags"(
    "course_id" INTEGER NOT NULL,
    "tag_id" INTEGER NOT NULL
);
ALTER TABLE
    "courses_tags" ADD PRIMARY KEY("course_id");
ALTER TABLE
    "courses_tags" ADD PRIMARY KEY("tag_id");
CREATE TABLE "tags"(
    "id" INTEGER NOT NULL,
    "name" INTEGER NOT NULL
);
ALTER TABLE
    "tags" ADD PRIMARY KEY("id");