$outputDocx = "D:\devops\student-management\docs\Test_Cases_Report_Student_Management.docx"
$tempDir = Join-Path $env:TEMP "docx_build_$(Get-Random)"
New-Item -ItemType Directory -Path $tempDir -Force | Out-Null
New-Item -ItemType Directory -Path "$tempDir\_rels" -Force | Out-Null
New-Item -ItemType Directory -Path "$tempDir\word\_rels" -Force | Out-Null

# 1. [Content_Types].xml
$contentTypes = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>
"@
[System.IO.File]::WriteAllText("$tempDir\[Content_Types].xml", $contentTypes, [System.Text.Encoding]::UTF8)

# 2. _rels/.rels
$rels = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>
"@
[System.IO.File]::WriteAllText("$tempDir\_rels\.rels", $rels, [System.Text.Encoding]::UTF8)

# 3. word/_rels/document.xml.rels
$docRels = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships"/>
"@
[System.IO.File]::WriteAllText("$tempDir\word\_rels\document.xml.rels", $docRels, [System.Text.Encoding]::UTF8)

# Helper function to generate Table Row
function New-DocxRow {
    param($cols, $isHeader = $false, $bg = "FFFFFF")
    $xml = "<w:tr>"
    foreach ($col in $cols) {
        $boldXml = if ($isHeader) { "<w:b/><w:color w:val='FFFFFF'/>" } else { "" }
        $fillXml = if ($isHeader) { "<w:shd w:val='clear' w:color='auto' w:fill='1E3C72'/>" } elseif ($bg -ne "FFFFFF") { "<w:shd w:val='clear' w:color='auto' w:fill='$bg'/>" } else { "" }
        $xml += @"
        <w:tc>
          <w:tcPr>
            <w:tcBorders>
              <w:top w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/>
              <w:left w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/>
              <w:bottom w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/>
              <w:right w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/>
            </w:tcBorders>
            $fillXml
            <w:tcMar>
              <w:top w:w="120" w:type="dxa"/>
              <w:bottom w:w="120" w:type="dxa"/>
              <w:left w:w="150" w:type="dxa"/>
              <w:right w:w="150" w:type="dxa"/>
            </w:tcMar>
          </w:tcPr>
          <w:p>
            <w:r>
              <w:rPr>
                <w:rFonts w:ascii="Calibri" w:hAnsi="Calibri"/>
                <w:sz w:val="19"/>
                $boldXml
              </w:rPr>
              <w:t>$col</w:t>
            </w:r>
          </w:p>
        </w:tc>
"@
    }
    $xml += "</w:tr>"
    return $xml
}

# 4. word/document.xml
$docXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
  <w:body>
    <w:p>
      <w:pPr><w:jc w:val="center"/><w:spacing w:after="100"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="44"/><w:color w:val="1E3C72"/><w:rFonts w:ascii="Calibri"/></w:rPr><w:t>DevOps-Based Student Management Web Application</w:t></w:r>
    </w:p>
    <w:p>
      <w:pPr><w:jc w:val="center"/><w:spacing w:after="300"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="30"/><w:color w:val="2A5298"/><w:rFonts w:ascii="Calibri"/></w:rPr><w:t>COMPREHENSIVE QUALITY ASSURANCE &amp; TEST EXECUTION REPORT</w:t></w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="200"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="22"/></w:rPr><w:t>Prepared By: </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="22"/></w:rPr><w:t>Member 5 (QA Engineer) &amp; DevOps Project Team (10 Members)</w:t></w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="200"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="22"/></w:rPr><w:t>Execution Framework: </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="22"/></w:rPr><w:t>JUnit 5, Spring Boot Test, Spring Security Test, MockMvc (Apache Maven 3.9 / Java 24)</w:t></w:r>
    </w:p>
    <w:p>
      <w:pPr><w:spacing w:after="300"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="22"/></w:rPr><w:t>Execution Summary: </w:t></w:r>
      <w:r><w:rPr><w:b/><w:color w:val="198754"/><w:sz w:val="22"/></w:rPr><w:t>10 Test Cases Executed | 10 Passed | 0 Failed | 0 Skipped (BUILD SUCCESS)</w:t></w:r>
    </w:p>

    <!-- Table Start -->
    <w:tbl>
      <w:tblPr>
        <w:tblW w:w="0" w:type="auto"/>
        <w:tblBorders>
          <w:top w:val="single" w:sz="6" w:space="0" w:color="1E3C72"/>
          <w:left w:val="single" w:sz="6" w:space="0" w:color="1E3C72"/>
          <w:bottom w:val="single" w:sz="6" w:space="0" w:color="1E3C72"/>
          <w:right w:val="single" w:sz="6" w:space="0" w:color="1E3C72"/>
          <w:insideH w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/>
          <w:insideV w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/>
        </w:tblBorders>
      </w:tblPr>
"@

$headers = @("Test ID", "Module", "Precondition", "Input / Action", "Expected Result", "Actual Result", "Status")
$docXml += New-DocxRow -cols $headers -isHeader $true

$testCases = @(
  @("TC-SEC-01", "Security & Auth", "User unauthenticated", "HTTP GET /dashboard", "Redirect to /login (HTTP 302)", "Redirected to **/login", "PASSED"),
  @("TC-SEC-02", "Security & Auth", "App running", "HTTP GET /login", "HTTP 200 OK, render login view", "HTTP 200 OK, login view returned", "PASSED"),
  @("TC-SEC-03", "Security & Auth", "User admin@college.edu exists", "HTTP POST /login with valid pass", "HTTP 302 Redirect to /dashboard", "Session created, redirected to /dashboard", "PASSED"),
  @("TC-SEC-04", "Security & Auth", "User admin@college.edu exists", "HTTP POST /login with wrong password", "HTTP 302 Redirect to /login?error=true", "Auth rejected, redirected with error param", "PASSED"),
  @("TC-REG-01", "Registration", "Candidate email not in DB", "registerUser(new candidate DTO)", "User saved with BCrypt encoded password", "User persisted, password hash verified", "PASSED"),
  @("TC-REG-02", "Registration", "Email admin@college.edu exists", "registerUser(duplicate email DTO)", "Throw IllegalArgumentException", "IllegalArgumentException thrown & caught", "PASSED"),
  @("TC-CRUD-01", "Student CRUD", "Authenticated user session", "HTTP GET /dashboard", "HTTP 200 OK, student list in model", "Model contains totalStudents & student list", "PASSED"),
  @("TC-CRUD-02", "Student CRUD", "Authenticated user session", "HTTP POST /students with new student", "Student saved, flash message created", "Student in DB, redirected with success flash", "PASSED"),
  @("TC-CRUD-03", "Student CRUD", "Saved student in repository", "Update name & Delete record", "Record updated, then removed completely", "Updated successfully; subsequent query empty", "PASSED"),
  @("TC-MON-01", "Monitoring", "App running with Actuator", "HTTP GET /actuator/health", "HTTP 200 OK, JSON status 'UP'", "HTTP 200 OK, status: UP (DB: UP, Disk: UP)", "PASSED")
)

$rowCount = 0
foreach ($tc in $testCases) {
    $bg = if ($rowCount % 2 -eq 1) { "F8FAFC" } else { "FFFFFF" }
    $docXml += New-DocxRow -cols $tc -isHeader $false -bg $bg
    $rowCount++
}

$docXml += @"
    </w:tbl>
    <!-- Table End -->

    <w:p><w:pPr><w:spacing w:before="300"/></w:pPr></w:p>
    <w:p>
      <w:pPr><w:spacing w:after="100"/></w:pPr>
      <w:r><w:rPr><w:b/><w:sz w:val="26"/><w:color w:val="1E3C72"/></w:rPr><w:t>Detailed Test Case Descriptions &amp; Observations</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>1. TC-SEC-01 (Route Authorization): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Ensured that unauthenticated requests to protected endpoints (/dashboard, /students/**) cannot bypass Spring Security filters. Verified via MockMvc perform(get("/dashboard")).status().is3xxRedirection().</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>2. TC-SEC-02 (Login Page Accessibility): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Confirmed the public availability of the login page without needing an existing session token. Verified response code HTTP 200 and view name resolution.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>3. TC-SEC-03 (Successful Authentication): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Submitted valid form credentials via POST /login. Successfully established an authenticated security session and redirected user to /dashboard.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>4. TC-SEC-04 (Bad Credentials Handling): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Attempted login with an invalid password. The authentication provider rejected the attempt and redirected back to /login?error=true to render a user-friendly alert message.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>5. TC-REG-01 (Registration &amp; Password Hashing): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Registered a new user candidate. Asserted that the password saved in the database is not in plain text and conforms to BCrypt salt/hash formatting.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>6. TC-REG-02 (Duplicate Account Prevention): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Attempted to re-register an existing email. Successfully threw IllegalArgumentException and prevented duplicate database entries.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>7. TC-CRUD-01 (Dashboard Data Rendering): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Accessed dashboard with @WithMockUser. Verified that total student count and model attributes are properly populated and rendered in Thymeleaf.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>8. TC-CRUD-02 (Student Record Creation): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Simulated POST /students form submission. Verified the record was inserted into the database and redirect flash message was added.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>9. TC-CRUD-03 (Update &amp; Delete Operations): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Updated a student's full name, verified changes persisted, and then executed deleteById() to verify complete removal.</w:t></w:r>
    </w:p>

    <w:p>
      <w:r><w:rPr><w:b/><w:sz w:val="21"/></w:rPr><w:t>10. TC-MON-01 (Actuator Health Check): </w:t></w:r>
      <w:r><w:rPr><w:sz w:val="21"/></w:rPr><w:t>Queried /actuator/health endpoint. Verified HTTP 200 response with JSON payload {"status":"UP"}, confirming database connectivity and disk health.</w:t></w:r>
    </w:p>

    <w:p><w:pPr><w:spacing w:before="200"/></w:pPr></w:p>
    <w:p>
      <w:pPr><w:jc w:val="center"/></w:pPr>
      <w:r><w:rPr><w:i/><w:sz w:val="18"/><w:color w:val="777777"/></w:rPr><w:t>— End of Quality Assurance Test Report —</w:t></w:r>
    </w:p>
  </w:body>
</w:document>
"@
[System.IO.File]::WriteAllText("$tempDir\word\document.xml", $docXml, [System.Text.Encoding]::UTF8)

# 5. Compress to .docx (OpenXML zip)
if (Test-Path $outputDocx) { Remove-Item -Force $outputDocx }
[System.IO.Compression.ZipFile]::CreateFromDirectory($tempDir, $outputDocx)
Remove-Item -Recurse -Force $tempDir

Write-Output "DOCX_CREATED_SUCCESSFULLY: $outputDocx"
