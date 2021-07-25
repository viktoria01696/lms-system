CREATE OR REPLACE FUNCTION update_rating() RETURNS TRIGGER AS $course_rating$
BEGIN
UPDATE courses SET rating = (SELECT avg(mark) FROM courses_rating where course_id = NEW."course_id") where id = NEW."course_id";
RETURN NEW;
END;
$course_rating$ LANGUAGE plpgsql;

CREATE TRIGGER courses
AFTER INSERT OR UPDATE OR DELETE ON courses_rating
FOR EACH ROW
EXECUTE PROCEDURE update_rating();
	
