CREATE TABLE docdcats (
    doc text not null,
    dcats text[],
    PRIMARY KEY(doc)
);
