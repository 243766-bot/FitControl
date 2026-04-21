# 💪 FitControl

**Aplicación Android nativa para la gestión integral de gimnasios.**

FitControl permite a los administradores de un gimnasio controlar socios, pagos, inventario de productos y ver reportes diarios, todo desde su dispositivo móvil. La app funciona en modo **offline-first**: los datos siempre están disponibles localmente y se sincronizan con la nube cuando hay conexión.

---

## ✨ Funcionalidades Principales

| Módulo | Descripción |
|---|---|
| 🔐 **Autenticación** | Registro e inicio de sesión con correo y contraseña. Sesión persistida en `SharedPreferences`. |
| 👤 **Gestión de Socios** | Alta, edición y baja de socios. Foto de perfil capturada con la cámara frontal del dispositivo. |
| 💳 **Pagos y Membresías** | Registro de membresías por socio con historial completo de pagos. |
| 📦 **Inventario** | Control de stock de productos (suplementos, etc.) con alerta de stock mínimo y registro de movimientos. |
| 📊 **Reportes Diarios** | Vista de ingresos, egresos y balance del día. |
| 📳 **Retroalimentación Háptica** | Vibración de confirmación al guardar un socio. |
| 🤳 **Detección de Agitación** | Gesto de "shake" para refrescar la lista de socios. |
| 🔑 **Autenticación Biométrica** | Infraestructura lista para autenticación por huella dactilar / reconocimiento facial. |

---

## 🏛️ Arquitectura

El proyecto sigue los principios de **Clean Architecture** con el patrón **MVVM**, organizado en capas bien definidas:

```
com.example.fitcontrol
│
├── core/                        # Código compartido entre features
│   ├── data/local/              # Room Database, DAOs, Entities
│   ├── navigation/              # AppNavigation, Screen (sealed interface)
│   └── util/                   # CameraCapture, ShakeDetector, BiometricHelper
│
├── di/                          # Módulos de inyección de dependencias (Hilt)
│   └── AppModule.kt             # NetworkModule + RepositoryModule
│
├── feature_auth/                # Módulo de autenticación
│   ├── data/                    # AuthApi (Retrofit), AuthRepositoryImpl
│   ├── domain/                  # Modelos (User, LoginRequest), AuthRepository
│   └── presentation/            # LoginScreen/VM, RegisterScreen/VM
│
├── feature_members/             # Módulo principal de socios e inventario
│   ├── data/                    # MemberApi, MemberRepositoryImpl
│   ├── domain/                  # Modelos, Use Cases, MemberRepository
│   └── presentation/            # Home, AddMember, EditMember, Payment,
│                                #   Inventory, Reports (Screen + ViewModel)
│
└── ui/                          # Tema global (Material 3), componentes comunes
```

### Flujo de datos

```
UI (Composable)
    │  collectAsState()
    ▼
ViewModel — StateFlow<UiState>
    │  viewModelScope.launch
    ▼
Use Case
    │
    ▼
Repository ──► Room (local, inmediato)
    │
    └──────► Retrofit / API REST (remoto, sincroniza Room)
```

---

## 🗄️ Esquema de Base de Datos (Room)

**Tabla `members`**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | String (PK) | Identificador único del socio |
| `user_id` | String | ID del gimnasio propietario |
| `name` | String | Nombre completo |
| `phone` | String | Teléfono de contacto |
| `email` | String | Correo electrónico |
| `birthDate` | String | Fecha de nacimiento |
| `photoUrl` | String? | URL de la foto de perfil |
| `status` | String | Estado: `ACTIVE` / `INACTIVE` |

**Tabla `products`**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Int (PK) | Identificador del producto |
| `user_id` | String | ID del gimnasio propietario |
| `name` | String | Nombre del producto |
| `category` | String | Categoría (ej. suplementos) |
| `buy_price` | Double | Precio de compra |
| `sell_price` | Double | Precio de venta |
| `stock` | Int | Cantidad en inventario |
| `min_stock` | Int | Umbral mínimo de alerta |

---

## 🌐 API REST (Endpoints)

Base URL: `http://18.205.240.72:3000/` (AWS EC2)

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/auth/login` | Iniciar sesión |
| `POST` | `/auth/register` | Registrar usuario |
| `GET` | `/members/{userId}` | Listar socios del gimnasio |
| `GET` | `/members/detail/{id}` | Detalle de un socio |
| `POST` | `/members` | Crear socio |
| `PUT` | `/members/{id}` | Actualizar socio |
| `DELETE` | `/members/{id}` | Eliminar socio |
| `POST` | `/memberships` | Registrar membresía/pago |
| `GET` | `/memberships/member/{id}` | Historial de pagos de un socio |
| `GET` | `/products/{userId}` | Listar productos |
| `POST` | `/products` | Crear/actualizar producto |
| `POST` | `/inventory/move` | Registrar movimiento de inventario |
| `GET` | `/reports/daily/{userId}` | Reporte diario |

---

## 🛠️ Stack Tecnológico

| Categoría | Tecnología | Versión |
|---|---|---|
| Lenguaje | Kotlin | 1.9.24 |
| UI | Jetpack Compose + Material 3 | 1.2.1 |
| Arquitectura | MVVM + Clean Architecture | — |
| Inyección de Dependencias | Hilt | 2.51.1 |
| Base de Datos Local | Room | 2.6.1 |
| Red | Retrofit 2 + Gson | 2.9.0 |
| Navegación | Navigation Compose (Type-Safe) | 2.8.0 |
| Cámara | CameraX | 1.3.3 |
| Carga de Imágenes | Coil | 2.6.0 |
| Biometría | AndroidX Biometric | 1.2.0 |
| Serialización | Kotlinx Serialization | 1.6.3 |
| Min SDK | Android 8.0 (Oreo) | API 26 |
| Target SDK | Android 15 | API 35 |

---

## 🚀 Configuración y Ejecución

### Prerrequisitos

- Android Studio Hedgehog o superior
- JDK 17
- Dispositivo o emulador con Android 8.0+ (API 26+)
- Conexión a internet para sincronización con la API (el modo offline funciona sin red)

### Pasos

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/FitControl.git
   ```

2. **Abre el proyecto en Android Studio.**

3. **Sincroniza las dependencias de Gradle** (Android Studio lo hace automáticamente).

4. **Ejecuta la app** con `Run > Run 'app'` o `Shift + F10`.

> ⚠️ La app se conecta a un servidor AWS. Si el servidor no está activo, la app opera en modo **offline** con los datos almacenados localmente en Room.

---

## 📂 Estructura del Proyecto Simplificada

```
FitControl/
├── app/
│   └── src/main/java/com/example/fitcontrol/
│       ├── core/
│       ├── di/
│       ├── feature_auth/
│       ├── feature_members/
│       ├── ui/
│       ├── FitControlApp.kt       ← @HiltAndroidApp
│       └── MainActivity.kt        ← @AndroidEntryPoint
├── gradle/
│   └── libs.versions.toml         ← Version Catalog
└── build.gradle.kts
```

---

## 👨‍💻 Autor

Victor Alfonso Alvarez Castillo 4B 243766
