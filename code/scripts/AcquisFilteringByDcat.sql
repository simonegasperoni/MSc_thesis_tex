create table dcatscounter AS
	SELECT 'ECON' as name, COUNT(*) FROM docdcats where array_length(dcats, 1)<=3 and dcats && '{ECON}'::text[]
	UNION
	SELECT 'REGI' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{REGI}'::text[]
	UNION
	SELECT 'AGRI' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{AGRI}'::text[]
	UNION
	SELECT 'HEAL' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{HEAL}'::text[]
	UNION
	SELECT 'TRAN' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{TRAN}'::text[]
	UNION
	SELECT 'JUST' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{JUST}'::text[]
	UNION
	SELECT 'SOCI' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{SOCI}'::text[]
	UNION
	SELECT 'GOVE' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{GOVE}'::text[]
	UNION
	SELECT 'INTR' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{INTR}'::text[]
	UNION
	SELECT 'ENVI' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{ENVI}'::text[]
	UNION
	SELECT 'TECH' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{TECH}'::text[]
	UNION
	SELECT 'EDUC' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{EDUC}'::text[]
	UNION
	SELECT 'ENER' as name, COUNT(*) FROM docdcats WHERE array_length(dcats, 1)<=3 and dcats && '{ENER}'::text[]
;
