# Stage 1: Zertifikate generieren (nutzt Alpine Linux mit OpenSSL)
FROM alpine:latest as cert-gen
RUN apk add --no-cache openssl
WORKDIR /certs
# Erstellt ein selbstsigniertes Zertifikat für "localhost", gültig für 365 Tage
RUN openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out cert.pem -subj "/CN=localhost"

RUN chmod 644 key.pem cert.pem

# Stage 2: Finales Image erstellen
FROM mccutchen/go-httpbin

# Zertifikate aus der ersten Stage in das finale Image kopieren
COPY --from=cert-gen /certs/cert.pem /cert.pem
COPY --from=cert-gen /certs/key.pem /key.pem

# Ports dokumentieren (8080 für HTTP, 8443 für HTTPS)
EXPOSE 8080 8443

# Startbefehl anpassen:
# Wir entfernen -port und -https-port, da die Standardwerte (8080 und 8443) bereits korrekt sind.
CMD ["/bin/go-httpbin", "-https-cert-file", "/cert.pem", "-https-key-file", "/key.pem"]