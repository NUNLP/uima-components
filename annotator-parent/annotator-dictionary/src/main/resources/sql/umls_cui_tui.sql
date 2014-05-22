select
	mrc.CUI
	, mrs.TUI
	, mrc.STR
from mrconso mrc
inner join
	umls.MRSTY as mrs
	on mrs.CUI = mrc.CUI
where
	mrs.cui = 'C0149862' -- villous adenoma
;
