#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   ./deploy/scripts/generate_sample_product_images.sh [output_dir]
#
# Example:
#   ./deploy/scripts/generate_sample_product_images.sh upload/productimage

OUTPUT_DIR="${1:-upload/productimage}"
mkdir -p "${OUTPUT_DIR}"

for i in $(seq 1 100); do
  n=$(printf "%03d" "${i}")
  f="${OUTPUT_DIR}/sample-${n}.svg"
  cat > "${f}" <<SVG
<svg xmlns="http://www.w3.org/2000/svg" width="600" height="600" viewBox="0 0 600 600">
  <defs>
    <linearGradient id="g${n}" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#e0f2fe"/>
      <stop offset="100%" stop-color="#bfdbfe"/>
    </linearGradient>
  </defs>
  <rect width="600" height="600" fill="url(#g${n})"/>
  <circle cx="300" cy="240" r="110" fill="#1d4ed8" opacity="0.15"/>
  <rect x="110" y="370" width="380" height="110" rx="22" fill="#ffffff" opacity="0.9"/>
  <text x="300" y="430" text-anchor="middle" font-size="34" font-family="Arial, sans-serif" fill="#1e3a8a">AUTO SAMPLE ${n}</text>
</svg>
SVG
done

echo "Generated 100 sample images in: ${OUTPUT_DIR}"
