-- filter courses by tag
SELECT * 
FROM courses 
WHERE  id in (SELECT course_id 
			  FROM courses_tags 
			  WHERE tag_id in (SELECT id 
							   FROM tags 
							   WHERE name ILIKE '%java%'));
							   
-- filter courses by category
SELECT * 
FROM courses 
WHERE  id in (SELECT course_id 
			  FROM courses_categories 
			  WHERE category_id in (SELECT id 
									FROM categories 
									WHERE title ILIKE '%java%'));
									
--sort courses by rating
SELECT title, author, description, rating
FROM courses
ORDER BY rating;

--sort courses by duration
SELECT title, author, description, rating
FROM courses
ORDER BY duration DESC;

--filter courses by levels 
SELECT *
FROM courses
WHERE (title || name || description) NOT ILIKE '%новичок%'

--filter users for first name
SELECT *
FROM courses
WHERE name ILIKE 'Ива%'

--filter by active tags
SELECT name
FROM tags
WHERE id in (SELECT tag_id
			 FROM courses_tags)
			 



 