# Data Model Specification

## Collections Structure

### `surveys` (Collection)
Stores metadata for each survey.

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique Survey ID (Auto-generated) |
| `title` | String | Survey Title |
| `description` | String | Survey Description |
| `isPublished` | Boolean | Visibility status. `true` = Public, `false` = Draft |
| `createdAt` | Timestamp | Creation time |

### `surveys/{surveyId}/questions` (Sub-collection)
Stores the questions for a specific survey.

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique Question ID (Auto-generated) |
| `text` | String | Question text |
| `type` | String | Enum: `SINGLE_CHOICE`, `MULTIPLE_CHOICE`, `TEXT` |
| `options` | Array<String> | List of choices (empty for TEXT type) |
| `order` | Number | Sort order index |

### `users` (Collection)
Stores user profiles and role information.

| Field | Type | Description |
|-------|------|-------------|
| `role` | String | `"admin"` for admin users |
| `enabled` | Boolean | Account status |

## Indexes
Standard single-field indexes are sufficient for current queries.
- `surveys` collection ordered by `createdAt` DESC requires an index if composite queries are added, but basic ordering is supported by default.

## Validation Rules
- `surveys`: `isPublished` must be boolean.
- `questions`: `type` must match valid enum values.
