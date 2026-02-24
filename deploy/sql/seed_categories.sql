set define off
set serveroutput on

merge into category c
using (
  select '1' category_code, '댕이용품' category_name, cast(null as varchar2(10)) super_category_code from dual union all
  select '2', '냥이용품', null from dual union all
  select '11', '푸드', '1' from dual union all
  select '12', '리빙', '1' from dual union all
  select '13', '외출', '1' from dual union all
  select '21', '푸드', '2' from dual union all
  select '22', '리빙', '2' from dual union all
  select '23', '외출', '2' from dual union all
  select '111', '사료', '11' from dual union all
  select '112', '간식', '11' from dual union all
  select '121', '장난감', '12' from dual union all
  select '122', '배변', '12' from dual union all
  select '211', '사료', '21' from dual union all
  select '212', '간식', '21' from dual union all
  select '221', '장난감', '22' from dual union all
  select '222', '모래/화장실', '22' from dual
) s
on (c.category_code = s.category_code)
when matched then
  update set c.category_name = s.category_name,
             c.super_category_code = s.super_category_code
when not matched then
  insert (category_code, category_name, super_category_code)
  values (s.category_code, s.category_name, s.super_category_code);

declare
  v_category_cnt number;
begin
  select count(*) into v_category_cnt from category;

  if v_category_cnt > 0 then
    update product p
       set p.category_code = (
         select category_code
           from (
             select category_code, row_number() over (order by category_code) rn
               from category
              where super_category_code is not null
           )
          where rn = mod(p.pno - 1, 14) + 1
       )
     where p.category_code is null;
  end if;

  commit;
  dbms_output.put_line('DONE: categories seeded and product category backfill completed.');
end;
/

prompt ==== CATEGORY COUNTS ====
select count(*) category_cnt from category;

prompt ==== ROOT CATEGORIES ====
select category_code, category_name
  from category
 where super_category_code is null
 order by category_code;

prompt ==== PRODUCT NULL CATEGORY ====
select count(*) null_category_products from product where category_code is null;
