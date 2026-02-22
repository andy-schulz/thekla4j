@echo off
:: Start Jekyll locally for development
:: Site will be available at http://127.0.0.1:4000

set PATH=C:\Ruby33-x64\bin;%PATH%
cd /d "%~dp0"

echo Starting Jekyll local server...
echo Site will be available at http://127.0.0.1:4000
echo Press Ctrl+C to stop.
echo.

bin\jekyll.cmd serve --config _config.yml,_config_local.yml
