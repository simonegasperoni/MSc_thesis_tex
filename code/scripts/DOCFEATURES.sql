CREATE TABLE docfeatures (
    doc text not null,
    features text[],
    PRIMARY KEY(doc)
);
