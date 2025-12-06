#!/usr/bin/env sh
set -ex

echo "⏳ Esperando 10 segundos para asegurar que backend esté listo..."
sleep 10

echo "➤ Creando usuario ADMIN_GENERAL..."
curl -X POST http://backend:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombreUsuario": "MarceloBarra",
    "contrasena": "12345",
    "tipoUsuario": "ADMIN_GENERAL"
  }' || echo "⚠ Aviso: el usuario ya existe o el endpoint falló"

echo "➤ Creando tabla local_tb e insertando locales iniciales..."

SQL="
INSERT INTO local_tb (nombre_local)
  SELECT 'tenedor_libre'
  WHERE NOT EXISTS (SELECT 1 FROM local_tb WHERE nombre_local='tenedor_libre');

INSERT INTO local_tb (nombre_local)
  SELECT 'termas'
  WHERE NOT EXISTS (SELECT 1 FROM local_tb WHERE nombre_local='termas');

INSERT INTO local_tb (nombre_local)
  SELECT 'heladeria'
  WHERE NOT EXISTS (SELECT 1 FROM local_tb WHERE nombre_local='heladeria');
"

echo "$SQL" | mysql -v -h db -u root -p${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE}

echo "✅ Script init-user completado."
