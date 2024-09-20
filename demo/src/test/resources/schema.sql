CREATE TABLE IF NOT EXISTS appuser (
                                      id INT NOT NULL AUTO_INCREMENT,
                                      username VARCHAR(30) NOT NULL,
                                      email VARCHAR(40) NOT NULL,
                                      password VARCHAR(255) NOT NULL,
                                      PRIMARY KEY (id),
                                      UNIQUE (username),
                                      UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS competition (
                                           id INT NOT NULL AUTO_INCREMENT,
                                           name VARCHAR(255) NOT NULL,
                                           description VARCHAR(255) NOT NULL,
                                           start_date DATE NOT NULL,
                                           end_date DATE,
                                           amount_of_winners INT NOT NULL,
                                           PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS idea (
                                    id INT NOT NULL AUTO_INCREMENT,
                                    Competition_id INT NOT NULL,
                                    User_id INT NOT NULL,
                                    title VARCHAR(255) NOT NULL,
                                    description VARCHAR(255),
                                    key_features VARCHAR(255),
                                    reference_links VARCHAR(255),
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    pictures VARCHAR(255),
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (Competition_id) REFERENCES competition(id),
                                    FOREIGN KEY (User_id) REFERENCES appuser(id)
);

CREATE TABLE IF NOT EXISTS bookmark (
                                        id INT NOT NULL AUTO_INCREMENT,
                                        User_id INT NOT NULL,
                                        Idea_id INT NOT NULL,
                                        PRIMARY KEY (id),
                                        FOREIGN KEY (Idea_id) REFERENCES idea(id),
                                        FOREIGN KEY (User_id) REFERENCES appuser(id)
);

CREATE TABLE IF NOT EXISTS category (
                                        id INT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(100) NOT NULL,
                                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comment (
                                       id INT NOT NULL AUTO_INCREMENT,
                                       User_id INT NOT NULL,
                                       Idea_id INT NOT NULL,
                                       content VARCHAR(255) NOT NULL,
                                       PRIMARY KEY (id),
                                       FOREIGN KEY (Idea_id) REFERENCES idea(id),
                                       FOREIGN KEY (User_id) REFERENCES appuser(id)
);

CREATE TABLE IF NOT EXISTS idea_category (
                                             Idea_id INT NOT NULL,
                                             Category_id INT NOT NULL,
                                             PRIMARY KEY (Idea_id, Category_id),
                                             FOREIGN KEY (Category_id) REFERENCES category(id),
                                             FOREIGN KEY (Idea_id) REFERENCES idea(id)
);

CREATE TABLE IF NOT EXISTS idea_selection (
                                              id INT NOT NULL AUTO_INCREMENT,
                                              Idea_id INT NOT NULL,
                                              Competition_id INT NOT NULL,
                                              date TIMESTAMP NOT NULL,
                                              PRIMARY KEY (id),
                                              FOREIGN KEY (Competition_id) REFERENCES competition(id),
                                              FOREIGN KEY (Idea_id) REFERENCES idea(id)
);

CREATE TABLE IF NOT EXISTS role (
                                    id INT NOT NULL AUTO_INCREMENT,
                                    name VARCHAR(40) NOT NULL,
                                    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role (
                                         Role_id INT NOT NULL,
                                         User_id INT NOT NULL,
                                         PRIMARY KEY (User_id, Role_id),
                                         FOREIGN KEY (Role_id) REFERENCES role(id),
                                         FOREIGN KEY (User_id) REFERENCES appuser(id)
);

CREATE TABLE IF NOT EXISTS votetype (
                                        id INT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(30) NOT NULL,
                                        points INT NOT NULL,
                                        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS vote (
                                    id INT NOT NULL AUTO_INCREMENT,
                                    VoteType_id INT NOT NULL,
                                    User_id INT NOT NULL,
                                    Idea_id INT NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (Idea_id) REFERENCES idea(id),
                                    FOREIGN KEY (User_id) REFERENCES appuser(id),
                                    FOREIGN KEY (VoteType_id) REFERENCES votetype(id)
);
CREATE TABLE user_selection_priorities (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           User_id INT NOT NULL,
                                           idea_selection_id INT DEFAULT NULL,
                                           Priority INT NOT NULL,
                                           submitted_at TIMESTAMP NOT NULL,
                                           CONSTRAINT fk_idea_selection_id FOREIGN KEY (idea_selection_id) REFERENCES idea_selection(id),
                                           CONSTRAINT Priority_user FOREIGN KEY (User_id) REFERENCES appuser(id)
);
CREATE TABLE user_selection_results (
                                        User_id INT NOT NULL,
                                        Idea_Selection_id INT NOT NULL,
                                        PRIMARY KEY (User_id, Idea_Selection_id),
                                        FOREIGN KEY (User_id) REFERENCES appuser(id),
                                        FOREIGN KEY (Idea_Selection_id) REFERENCES idea_selection(id)
);