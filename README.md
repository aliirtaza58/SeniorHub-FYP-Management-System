# SeniorHub - FYP Management System

A centralized digital platform for final year students to manage Final Year Projects (FYPs), find supervisors, and track graduation clearance. 

This implementation covers **all functional requirements** outlined in the SDA Assignment (FR-01 through FR-19), mapping the initial UML design to a full Java Swing desktop application.

## Features

- **Multi-Role Portals**: Dedicated dashboards for Students, Faculty, and System Admins.
- **Student Dashboard**: 
  - Manage profile and FYP groups.
  - Browse faculty directory and send supervision requests.
  - View project milestones and submit deliverables.
  - Apply to job postings and message alumni.
  - Track graduation clearance checklist and request transcripts.
- **Faculty Dashboard**: Review/grade submitted deliverables and manage incoming supervision requests.
- **Admin Dashboard**: Manage job postings and update student clearance statuses across departments.
- **Design Patterns**: Implements **MVC** (separating GUI from domain logic) and **Singleton** (`AppData` class) for consistent state management.

## Prerequisites

- **None.** A bundled JDK 25 (Eclipse Temurin) is included in the `jdk/` folder.
- No system-wide Java installation required. Nothing is installed to your user or system directories.

## How to Compile and Run

### Option A — One-Click (Recommended)

Double-click `run-local.bat` in File Explorer, or run from PowerShell:

```powershell
& ".\run-local.bat"
```

The script will automatically compile and launch the application using the bundled JDK.

### Option B — Manual Steps (PowerShell)

**Step 1 — Navigate to the project folder**
```powershell
Set-Location "Your Project Path"
```

**Step 2 — Compile**
```powershell
& ".\jdk\bin\javac.exe" SeniorHubApp.java
```

**Step 3 — Run**
```powershell
& ".\jdk\bin\javaw.exe" -cp . SeniorHubApp
```
> Use `java.exe` instead of `javaw.exe` if you want to see console output/errors.

### On Ubuntu / Linux

```bash
javac SeniorHubApp.java && java SeniorHubApp
```

## Testing Credentials

The system comes pre-loaded with mock data for testing. You can use any of the following accounts:

### Student Accounts (Password: `pass123`)
- `hammad@uni.edu`
- `muqeet@uni.edu`
- `irtaza@uni.edu`

### Faculty Accounts (Password: `admin123`)
- `smith@uni.edu`
- `jane@uni.edu`
- `ahmed@uni.edu`

### Admin Account
- **Email**: `admin@uni.edu`
- **Password**: `admin123`
