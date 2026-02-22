#!/usr/bin/env bash
set -euo pipefail

# Seeds 100 demo products into Oracle XE and generates matching sample images.
# Prerequisites:
# - docker compose oracle-xe is running
# - .env contains ORACLE_APP_USER / ORACLE_APP_PASSWORD
#
# Usage:
#   ./deploy/scripts/seed_products_100.sh

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "${ROOT_DIR}"

if [[ ! -f ".env" ]]; then
  echo "Missing .env in project root."
  exit 1
fi

set -a
source .env
set +a

: "${ORACLE_APP_USER:?ORACLE_APP_USER is required in .env}"
: "${ORACLE_APP_PASSWORD:?ORACLE_APP_PASSWORD is required in .env}"

./deploy/scripts/generate_sample_product_images.sh "upload/productimage"

docker cp deploy/sql/seed_products_100.sql cdmall-oracle:/tmp/seed_products_100.sql
docker exec -i cdmall-oracle \
  sqlplus "${ORACLE_APP_USER}/${ORACLE_APP_PASSWORD}@//localhost:1521/XEPDB1" \
  @/tmp/seed_products_100.sql

echo
echo "Done."
echo "- Seeded products: AUTO-SAMPLE-001..100"
echo "- Images generated : upload/productimage/sample-001.svg..sample-100.svg"
