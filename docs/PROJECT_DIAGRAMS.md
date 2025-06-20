# 📊 Vehicle Tracker - Mermaid Diagrams

**Developer**: Abdullah Abdelhakeem (abdullah.abdelhakeem25@gmail.com)

## 1. Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant MainActivity
    participant DashboardScreen
    participant MapsScreen
    participant TripListScreen
    participant SettingsScreen

    User->>MainActivity: Launch App
    MainActivity->>DashboardScreen: Navigate to Dashboard
    DashboardScreen->>User: Display Statistics & Quick Actions

    alt Dashboard Interactions
        User->>DashboardScreen: Toggle Sample Data
        DashboardScreen->>User: Show Updated Statistics
        User->>DashboardScreen: Click "Start Trip"
        DashboardScreen->>MapsScreen: Navigate to Maps
    end

    alt Maps - Live Tracking
        User->>MapsScreen: Click "Start Tracking"
        loop Real-time Updates
            MapsScreen->>MapsScreen: Update Speed/Distance
            MapsScreen->>User: Display Live Statistics
        end
        User->>MapsScreen: Click "Save Trip"
        MapsScreen->>User: Show Success Message
    end

    alt Trip Management
        User->>TripListScreen: Type in Search
        TripListScreen->>User: Show Filtered Results
        User->>TripListScreen: Click Trip Card
        TripListScreen->>User: Navigate to Details
    end
```

## 2. Class Diagram

```mermaid
classDiagram
    class MainActivity {
        +onCreate()
        +setContent()
        -VehicleTrackerNavigation()
    }

    class DashboardScreen {
        +DashboardScreen()
        -sampleDataCreated: Boolean
        +getInteractiveStatsCards()
        +QuickActionCard()
    }

    class MapsScreen {
        +MapsScreen()
        -isTracking: Boolean
        -currentSpeed: Float
        -distance: Float
        -routePoints: Int
        +LocationMarker()
    }

    class TripListScreen {
        +TripListScreen()
        -searchQuery: String
        -trips: List~TripItem~
        +getSampleTrips()
    }

    class TripItem {
        +id: Int
        +name: String
        +startAddress: String
        +endAddress: String
        +distance: Double
        +duration: Long
    }

    MainActivity --> DashboardScreen
    MainActivity --> MapsScreen
    MainActivity --> TripListScreen
    TripListScreen ..> TripItem : uses
```

## 3. Navigation Flow

```mermaid
flowchart TD
    A[App Launch] --> B[MainActivity]
    B --> C[Dashboard Screen]
    
    C --> D{User Action}
    D -->|Start Trip| F[Maps Screen]
    D -->|View History| G[Trip List Screen]
    D -->|Settings| H[Settings Screen]
    
    F --> I{Maps Actions}
    I -->|Start Tracking| J[Begin Simulation]
    I -->|Save Trip| L[Create Trip Record]
    I -->|Back| C
    
    G --> O{Trip Actions}
    O -->|Search| P[Filter Results]
    O -->|Click Trip| Q[Trip Detail Screen]
    O -->|Back| C
    
    H --> T{Settings Actions}
    T -->|Toggle Dark Mode| U[Update Theme]
    T -->|Back| C
    
    style C fill:#f3e5f5
    style F fill:#e8f5e8
    style G fill:#fff3e0
    style H fill:#fce4ec
```

## 4. Component Architecture

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[MainActivity] 
        B[DashboardScreen]
        C[MapsScreen]
        D[TripListScreen]
        E[SettingsScreen]
    end
    
    subgraph "UI Components"
        F[Navigation]
        G[Material 3]
        H[Custom Composables]
    end
    
    subgraph "Data Layer"
        I[Local State]
        J[Sample Data]
        K[TripItem Model]
    end
    
    subgraph "Platform"
        L[Android Automotive]
        M[Jetpack Compose]
        N[Material Design 3]
    end
    
    A --> F
    B --> G
    C --> H
    D --> G
    E --> G
    
    B --> I
    C --> I
    D --> J
    
    F --> M
    G --> N
    M --> L
```

## 5. State Diagram

```mermaid
stateDiagram-v2
    [*] --> Dashboard
    
    state Dashboard {
        [*] --> DefaultStats
        DefaultStats --> SampleStats : Toggle
        SampleStats --> DefaultStats : Toggle
    }
    
    Dashboard --> Maps : Start Trip
    Dashboard --> TripList : View History
    Dashboard --> Settings : Open Settings
    
    state Maps {
        [*] --> MapIdle
        MapIdle --> TrackingActive : Start
        TrackingActive --> MapIdle : Stop
        
        state TrackingActive {
            [*] --> Simulating
            Simulating --> Simulating : Update
        }
    }
    
    state TripList {
        [*] --> ShowingTrips
        ShowingTrips --> FilteringTrips : Search
        FilteringTrips --> ShowingTrips : Clear
    }
    
    Maps --> Dashboard : Back
    TripList --> Dashboard : Back
    Settings --> Dashboard : Back
```

---

**Usage**: Copy any diagram code and use in GitHub README or Mermaid Live Editor
