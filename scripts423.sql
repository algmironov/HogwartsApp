SELECT students.name , students.age, faculties.name
FROM students
INNER JOIN faculties ON faculty_id = faculties.id;

SELECT students.name, students.age, avatar_id
FROM students
INNER JOIN avatars ON students.avatar_id = avatars.id;