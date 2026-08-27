#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

echo "=== Larch — Install ==="

# Download external assets if not already present
if [ ! -f src/main/resources/static/css/bootstrap.min.css ]; then
    echo "Downloading Bootstrap CSS..."
    curl -sL "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" -o src/main/resources/static/css/bootstrap.min.css
fi

if [ ! -f src/main/resources/static/js/bootstrap.bundle.min.js ]; then
    echo "Downloading Bootstrap JS..."
    curl -sL "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" -o src/main/resources/static/js/bootstrap.bundle.min.js
fi

if [ ! -f src/main/resources/static/js/chart.umd.min.js ]; then
    echo "Downloading Chart.js..."
    curl -sL "https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js" -o src/main/resources/static/js/chart.umd.min.js
fi

# Download fonts if not present
if [ ! "$(ls -A src/main/resources/static/fonts 2>/dev/null)" ]; then
    echo "Downloading fonts..."
    curl -sL "https://fonts.googleapis.com/css2?family=Fraunces:opsz,wght@9..144,400;9..144,500;9..144,600;9..144,700&display=swap" -o /tmp/fraunces.css
    curl -sL "https://fonts.googleapis.com/css2?family=Newsreader:opsz,wght@6..72,400;6..72,500;6..72,600&display=swap" -o /tmp/newsreader.css
    cat /tmp/fraunces.css /tmp/newsreader.css | grep -oP 'url\(https://[^)]+\)' | sed 's/url(//;s/)//' | while read url; do
        fname=$(basename "$url" | cut -d'?' -f1)
        curl -sL "$url" -o "src/main/resources/static/fonts/$fname"
    done
    echo "Fonts downloaded."
fi

# Compile the application
echo "Compiling Kotlin sources..."
sh gradlew classes --no-daemon -q

echo ""
echo "=== Install complete ==="
echo "Run the server with: sh gradlew run --console=plain"
echo "Then open http://localhost:8080"
