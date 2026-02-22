set serveroutput on
set define off

declare
  v_writer varchar2(50);

  v_board_cnt number := 0;
  v_notice_cnt number := 0;
  v_qb_cnt number := 0;
  v_ub_cnt number := 0;
  v_ib_cnt number := 0;
  v_review_cnt number := 0;
  v_product_cnt number := 0;

  v_comment_cnt number := 0;
  v_qscomment_cnt number := 0;
  v_usedcomment_cnt number := 0;
  v_ibcomment_cnt number := 0;

  v_need number := 0;
  v_ref_id number := 0;
begin
  begin
    select username
      into v_writer
      from (
        select username from member where enabled = 1 order by username
      )
     where rownum = 1;
  exception
    when no_data_found then
      dbms_output.put_line('SKIP: no enabled member found. seed member first.');
      return;
  end;

  select count(*) into v_product_cnt from product;
  if v_product_cnt = 0 then
    dbms_output.put_line('SKIP: product table is empty. seed products first.');
    return;
  end if;

  select count(*) into v_board_cnt from board;
  v_need := greatest(0, 40 - v_board_cnt);
  for i in 1 .. v_need loop
    insert into board (
      bno, create_time, update_time, attachment_cnt, bad_cnt, category, comment_cnt,
      content, good_cnt, is_active, read_cnt, title, warn_cnt, writer
    ) values (
      board_seq.nextval, systimestamp, systimestamp, 0, mod(i, 3),
      case mod(i, 4) when 0 then '유머' when 1 then '잡담' when 2 then '댕댕이' else '고양이' end,
      0,
      '자유게시판 샘플 본문 #' || to_char(v_board_cnt + i),
      mod(i, 9), 1, mod(i, 120),
      '자유게시판 샘플 #' || to_char(v_board_cnt + i),
      0, v_writer
    );
  end loop;

  select count(*) into v_notice_cnt from notice_board;
  v_need := greatest(0, 12 - v_notice_cnt);
  for i in 1 .. v_need loop
    insert into notice_board (
      nbno, create_time, update_time, content, read_cnt, title, writer
    ) values (
      notice_board_seq.nextval, systimestamp, systimestamp,
      '공지사항 샘플 본문 #' || to_char(v_notice_cnt + i),
      mod(i, 300),
      '공지사항 샘플 #' || to_char(v_notice_cnt + i),
      v_writer
    );
  end loop;

  select count(*) into v_qb_cnt from question_board;
  v_need := greatest(0, 25 - v_qb_cnt);
  for i in 1 .. v_need loop
    insert into question_board (
      qbno, create_time, update_time, attachment_cnt, bad_cnt, comment_cnt,
      content, good_cnt, read_cnt, title, writer
    ) values (
      question_board_seq.nextval, systimestamp, systimestamp, 0, mod(i, 2), 0,
      '질문게시판 샘플 본문 #' || to_char(v_qb_cnt + i),
      mod(i, 7), mod(i, 90),
      '질문게시판 샘플 #' || to_char(v_qb_cnt + i),
      v_writer
    );
  end loop;

  select count(*) into v_ub_cnt from used_board;
  v_need := greatest(0, 25 - v_ub_cnt);
  for i in 1 .. v_need loop
    insert into used_board (
      ubno, create_time, update_time, attachment_cnt, bad_cnt, comment_cnt,
      content, good_cnt, read_cnt, title, warn_cnt, writer
    ) values (
      used_board_seq.nextval, systimestamp, systimestamp, 0, mod(i, 2), 0,
      '중고게시판 샘플 본문 #' || to_char(v_ub_cnt + i),
      mod(i, 6), mod(i, 70),
      '중고게시판 샘플 #' || to_char(v_ub_cnt + i),
      0, v_writer
    );
  end loop;

  select count(*) into v_ib_cnt from image_board;
  v_need := greatest(0, 25 - v_ib_cnt);
  for i in 1 .. v_need loop
    insert into image_board (
      ibno, create_time, update_time, check_cnt, content, good_cnl_cnt, good_cnt,
      ibcomment_cnt, image_file_name, is_active, report_cnt, title, writer
    ) values (
      image_board_seq.nextval, systimestamp, systimestamp, 0,
      '사진게시판 샘플 본문 #' || to_char(v_ib_cnt + i),
      0, mod(i, 8), 0,
      'sample-' || lpad(to_char(mod(i, 100) + 1), 3, '0') || '.svg',
      1, 0,
      '사진게시판 샘플 #' || to_char(v_ib_cnt + i),
      v_writer
    );
  end loop;

  select count(*) into v_board_cnt from board;
  select count(*) into v_comment_cnt from comments;
  v_need := greatest(0, 120 - v_comment_cnt);
  if v_board_cnt > 0 and v_need > 0 then
    for i in 1 .. v_need loop
      select bno
        into v_ref_id
        from (
          select bno, row_number() over(order by bno) rn
          from board
        )
       where rn = mod(i - 1, v_board_cnt) + 1;

      insert into comments (cno, create_time, content, profile, writer, bno)
      values (comment_seq.nextval, systimestamp, '자유게시판 댓글 샘플 #' || to_char(v_comment_cnt + i), 'default.png', v_writer, v_ref_id);
    end loop;
  end if;

  select count(*) into v_qb_cnt from question_board;
  select count(*) into v_qscomment_cnt from qscomments;
  v_need := greatest(0, 80 - v_qscomment_cnt);
  if v_qb_cnt > 0 and v_need > 0 then
    for i in 1 .. v_need loop
      select qbno
        into v_ref_id
        from (
          select qbno, row_number() over(order by qbno) rn
          from question_board
        )
       where rn = mod(i - 1, v_qb_cnt) + 1;

      insert into qscomments (qbcno, create_time, content, profile, writer, qbno)
      values (qscomment_seq.nextval, systimestamp, '질문게시판 댓글 샘플 #' || to_char(v_qscomment_cnt + i), 'default.png', v_writer, v_ref_id);
    end loop;
  end if;

  select count(*) into v_ub_cnt from used_board;
  select count(*) into v_usedcomment_cnt from usedcomments;
  v_need := greatest(0, 80 - v_usedcomment_cnt);
  if v_ub_cnt > 0 and v_need > 0 then
    for i in 1 .. v_need loop
      select ubno
        into v_ref_id
        from (
          select ubno, row_number() over(order by ubno) rn
          from used_board
        )
       where rn = mod(i - 1, v_ub_cnt) + 1;

      insert into usedcomments (ubcno, create_time, content, profile, writer, ubno)
      values (usedcomment_seq.nextval, systimestamp, '중고게시판 댓글 샘플 #' || to_char(v_usedcomment_cnt + i), 'default.png', v_writer, v_ref_id);
    end loop;
  end if;

  select count(*) into v_ib_cnt from image_board;
  select count(*) into v_ibcomment_cnt from ibcomments;
  v_need := greatest(0, 80 - v_ibcomment_cnt);
  if v_ib_cnt > 0 and v_need > 0 then
    for i in 1 .. v_need loop
      select ibno
        into v_ref_id
        from (
          select ibno, row_number() over(order by ibno) rn
          from image_board
        )
       where rn = mod(i - 1, v_ib_cnt) + 1;

      insert into ibcomments (ibcno, create_time, content, profile, writer, ibno)
      values (ibcomment_seq.nextval, systimestamp, '사진게시판 댓글 샘플 #' || to_char(v_ibcomment_cnt + i), 'default.png', v_writer, v_ref_id);
    end loop;
  end if;

  select count(*) into v_review_cnt from reviews;
  v_need := greatest(0, 120 - v_review_cnt);
  if v_need > 0 then
    for i in 1 .. v_need loop
      select pno
        into v_ref_id
        from (
          select pno, row_number() over(order by pno) rn
          from product
        )
       where rn = mod(i - 1, v_product_cnt) + 1;

      insert into reviews (
        rno, create_time, attachment_cnt, bad_cnt, comment_cnt, content, good_cnt,
        image_file_name, is_active, read_cnt, star, warn_cnt, writer, username, pno
      ) values (
        review_seq.nextval, systimestamp, 0, 0, 0,
        '리뷰 샘플 #' || to_char(v_review_cnt + i) || ' - 만족도 ' || to_char(mod(i, 5) + 1),
        mod(i, 4), null, 1, mod(i, 25), mod(i, 5) + 1, 0, v_writer, v_writer, v_ref_id
      );
    end loop;
  end if;

  update board b
     set b.comment_cnt = (select count(*) from comments c where c.bno = b.bno);

  update question_board q
     set q.comment_cnt = (select count(*) from qscomments c where c.qbno = q.qbno);

  update used_board u
     set u.comment_cnt = (select count(*) from usedcomments c where c.ubno = u.ubno);

  update image_board i
     set i.ibcomment_cnt = (select count(*) from ibcomments c where c.ibno = i.ibno);

  update product p
     set p.review_count = (select count(*) from reviews r where r.pno = p.pno),
         p.count_of_star = (select count(*) from reviews r where r.pno = p.pno),
         p.sum_of_star = (select nvl(sum(r.star), 0) from reviews r where r.pno = p.pno),
         p.avg_of_star = (
           select case when count(*) = 0 then 0 else round(avg(r.star), 2) end
           from reviews r
           where r.pno = p.pno
         );

  commit;
  dbms_output.put_line('DONE: community + review seed applied. writer=' || v_writer);
end;
/

prompt ==== COUNTS AFTER SEED ====
select 'BOARD' t, count(*) c from board
union all select 'NOTICE_BOARD', count(*) from notice_board
union all select 'QUESTION_BOARD', count(*) from question_board
union all select 'USED_BOARD', count(*) from used_board
union all select 'IMAGE_BOARD', count(*) from image_board
union all select 'COMMENTS', count(*) from comments
union all select 'QSCOMMENTS', count(*) from qscomments
union all select 'USEDCOMMENTS', count(*) from usedcomments
union all select 'IBCOMMENTS', count(*) from ibcomments
union all select 'REVIEWS', count(*) from reviews
order by 1;
