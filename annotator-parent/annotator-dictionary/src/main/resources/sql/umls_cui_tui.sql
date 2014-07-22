select 
	mrc.CUI
	, mrs.TUI
	, mrc.CODE
	, mrc.STR 
from 
	mrconso as mrc 
inner join 
	umls.MRSTY as mrs 
	on mrs.CUI = mrc.CUI 
where 
	mrs.CUI in ('C0206677')
;

select
	mr.*
from 
	mrconso as mr
where
	mr.str like '%adenoma%'
limit 100
;