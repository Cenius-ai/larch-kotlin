# Usage

Once the application is running (via `./gradlew run`), open a browser and navigate to [http://localhost:8080](http://localhost:8080) (or the custom port you set).

## Dashboard

The landing page shows a summary of your total portfolio value and an allocation pie chart. This data is seeded with demo holdings.

## Holdings

Lists all current holdings with their name, amount, and value. Each holding includes a delete button to remove it from the portfolio.

## Add Holding

Navigate to the "Add Holding" page to fill a form with the asset name, amount, and current value. Submitting the form adds the new holding to the portfolio and to the database.

## Theme Toggle

Use the theme toggle button (visible in the navigation) to switch between light and dark modes. Your preference persists across pages.

## Navigation

The layout provides links to all pages:
- Dashboard (`/`)
- Holdings
- Add Holding

All actions happen via standard web forms and links; no special command-line interaction is required.