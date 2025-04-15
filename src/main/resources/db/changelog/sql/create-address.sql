-- Table address
CREATE TABLE address (
    id BIGINT,
    city VARCHAR(255),
    author_id BIGINT
);

-- Foreign key for author-address
ALTER TABLE address
ADD CONSTRAINT fk_address_author
FOREIGN KEY (author_id)
REFERENCES author(id);