-- 50 sample products for portfolio/demo
-- Safe to run multiple times: skips rows if the same product name already exists.

DECLARE
  v_category_code category.category_code%TYPE;
  v_category_count NUMBER := 0;
  v_price NUMBER;
  v_stock NUMBER;
  v_good NUMBER;
  v_sales NUMBER;
  v_name VARCHAR2(50);
  v_exists NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_category_count FROM category WHERE category_code IS NOT NULL;

  FOR i IN 1..50 LOOP
    v_name := 'AUTO-샘플상품-' || LPAD(i, 2, '0');
    SELECT COUNT(*) INTO v_exists FROM product WHERE name = v_name;

    IF v_exists = 0 THEN
      IF v_category_count > 0 THEN
        SELECT category_code
          INTO v_category_code
          FROM (
            SELECT category_code, ROW_NUMBER() OVER (ORDER BY category_code) rn
              FROM category
             WHERE category_code IS NOT NULL
          )
         WHERE rn = MOD(i - 1, v_category_count) + 1;
      ELSE
        v_category_code := NULL;
      END IF;

      v_price := 7000 + (i * 900);
      v_stock := 20 + MOD(i, 40);
      v_good := MOD(i * 3, 25);
      v_sales := MOD(i * 11, 120);

      INSERT INTO product (
        pno, manufacturer, name, image, info, writer, good_cnt, good_cnl_cnt,
        price, sales_volume, sales_amount, count_of_star, sum_of_star, avg_of_star,
        review_count, stock, image_file_name, category_code, create_time, update_time
      ) VALUES (
        product_seq.NEXTVAL,
        'AUTO_BRAND_' || TO_CHAR(MOD(i, 5) + 1),
        v_name,
        'sample-' || LPAD(i, 2, '0') || '.jpg',
        '포트폴리오 데모용 자동 등록 상품 #' || i,
        'KINGSTONE1',
        v_good,
        0,
        v_price,
        v_sales,
        v_price * v_sales,
        0,
        0,
        0,
        0,
        v_stock,
        'missing-sample-' || LPAD(i, 2, '0') || '.jpg',
        v_category_code,
        SYSTIMESTAMP - NUMTODSINTERVAL(i, 'HOUR'),
        SYSTIMESTAMP
      );
    END IF;
  END LOOP;

  COMMIT;
END;
/

SELECT COUNT(*) AS auto_seed_count
FROM product
WHERE name LIKE 'AUTO-샘플상품-%';
