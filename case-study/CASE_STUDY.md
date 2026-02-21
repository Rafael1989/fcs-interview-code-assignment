# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
```
KEY CHALLENGES:
1. Multi-Dimensional Cost Attribution
   - Same product travels through multiple warehouses before reaching a store
   - How to allocate shipping cost when multiple items are consolidated?
   - Shared overhead (e.g., supervisor salary) needs proportional allocation
   
2. Real-time vs Batch Processing
   - Should we track in real-time or consolidate daily/weekly?
   - Real-time = complex, accurate; Batch = simple, delayed insights
   
3. Direct vs Indirect Costs
   - Direct: warehouse rent, staff salary (clear attribution)
   - Indirect: HQ overhead, insurance, depreciation (harder to allocate)
   
4. Allocation Methodology
   - By transaction volume? By product weight? By warehouse capacity used?
   - Different methodologies = different P&L results

CRITICAL QUESTIONS TO ASK:
- What is the cost allocation purpose? (Pricing decisions, performance measurement, budget control?)
- How frequently do allocation rules change? (seasonal, promotional, regulatory?)
- Do we need drill-down capability? (CEO level vs warehouse manager level)
- What's the tolerance for estimation vs actual? (±5%, ±10%?)

INFORMATION NEEDED:
- Current cost tracking capabilities
- Regulatory requirements for cost reporting
- Desired reporting frequency and detail level

PERSONAL EXPERIENCE & RECOMMENDED APPROACH:
From my perspective, I've observed that companies often fail in cost allocation by trying to implement 
complex Activity-Based Costing (ABC) systems immediately. Instead, I recommend a phased approach:

PHASE 1 (MVP - Month 1-2): Start Simple
- Implement basic transaction logging with daily batch processing
- Use simple methodology: allocation by transaction volume (easiest to implement)
- Build reconciliation dashboard showing warehouse costs vs GL (finance visibility)
- Success metric: Finance can see cost breakdown in real-time, variance <5%

PHASE 2 (Production - Month 3-4): Refine
- Analyze if volume-based allocation matches business reality
- Evolve to weight-based or capacity-based allocation if needed
- Implement Data Warehouse for historical analysis and trend tracking
- Success metric: Cost allocation rules validated against actual P&L impact

TECHNICAL STACK RECOMMENDATION:
- Event capture: Transaction logs or Apache Kafka for real-time events
- Processing: Daily batch or near real-time depending on finance requirements
- Storage: Data Warehouse (Snowflake/BigQuery) for analytics
- Visibility: BI tool (Tableau/Power BI) for finance dashboard

KEY INSIGHT:
The "right" allocation methodology is less important than having ANY methodology that:
1. Is consistent and auditable
2. Aligns with business decision-making needs
3. Can be refined over time based on actual results
```

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
```
IDENTIFICATION STRATEGIES:

1. Data-Driven Analysis
   - Compare warehouse utilization metrics (cost per SKU, cost per transaction)
   - Identify underutilized facilities (consolidate operations?)
   - Analyze transportation costs by route/frequency pattern
   
2. Benchmarking
   - Compare your costs against industry standards
   - Which warehouses underperform peers? Why?
   - What best practices can be adopted?

3. Process Optimization Areas
   - Automation: Pick/pack automation typically reduces labor 30-40%
   - Inventory placement: Cross-dock products before storage (reduce handling)
   - Consolidation: Route optimization reduces empty truck miles
   - Staffing: Predictive staffing based on seasonal demand

PRIORITIZATION FRAMEWORK:
High Impact + Low Effort → Execute First (Quick wins)
- Route optimization, staff scheduling adjustments
- Expected: 5-10% transport cost reduction in 6 months

High Impact + High Effort → Strategic Projects (12-18 months)
- Warehouse automation, system integration
- Expected: 15-25% labor cost reduction annually

IMPLEMENTATION APPROACH:
1. Pilot: Test optimization in one warehouse/route
2. Measure: Track cost/quality metrics before/after
3. Scale: If successful, roll out to other locations
4. Iterate: Continuously monitor and refine

KEY SUCCESS FACTOR:
Set guardrails on service quality metrics (delivery time, damage rate, order accuracy)
→ If these slip, cost savings don't matter (customer churn)

ROI & INVESTMENT ANALYSIS:
Based on typical fulfillment operations, here's the business case:

Quick Wins (Months 1-6):
- Route optimization: Investment $500K → Annual savings $600K+ (ROI: 10 months)
- Staff scheduling: Investment $100K → Annual savings $200K+ (ROI: 6 months)
- Quality metrics protection: Keep damage rate <2%, delivery time <48 hours

Strategic Projects (Months 6-18):
- Pick/pack automation: Investment $2-3M → Annual savings $3-4M+ (ROI: 9-12 months)
- Warehouse consolidation: Investment $1M migration → Annual savings $500K+ (ongoing)
- System integration: Investment $300K → Annual savings $150K + better decision-making

CHANGE MANAGEMENT CONSIDERATIONS:
Cost optimization often fails not due to poor ideas, but poor change management. Key mitigations:

1. Stakeholder Buy-in
   - Warehouse managers fear automation = job loss. Reality: Need them for new roles (optimization, quality)
   - Communicate redeployment strategy early
   - Offer training for higher-value roles

2. Phased Rollout (not big-bang)
   - Pilot in one location (build confidence)
   - Document success stories and cost savings
   - Address concerns before scaling

3. Performance Incentives
   - Align bonuses with both cost reduction AND quality metrics
   - Avoid scenario where staff cuts corners to hit cost targets

4. Communications Plan
   - Month 1-2: Awareness (why cost optimization is necessary)
   - Month 3-4: Pilot selection and training
   - Month 5+: Success stories and momentum building
```

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
```
IMPORTANCE & BENEFITS:

1. Single Source of Truth
   Problem: Without integration, warehouse costs tracked separately from GL (General Ledger)
   → Cost control system shows $100K, GL shows $95K → Which is correct?
   Benefit: Integrated system = one authoritative cost figure for decisions
   Impact: Finance reports match operational reports (no reconciliation headaches)

2. Real-time Decision Making
   Without integration: Finance gets cost data monthly, 2 weeks late
   → Can't adjust operations quickly to budget overruns
   Benefit: Integrated system pushes costs to GL hourly/daily
   Impact: Managers see budget variances in real-time, can act immediately

3. Compliance & Audit
   Benefit: Integrated system creates audit trail (who changed what, when)
   Impact: Regulatory requirements (SOX, IFRS) automatically satisfied
   
4. Elimination of Manual Work
   Without: Finance staff manually reconciles operational costs with GL
   Benefit: Automation reduces manual errors (typically 2-5% discrepancies)
   Impact: Finance team focuses on analysis instead of data entry

ENSURING SEAMLESS INTEGRATION:

Technical Approach:
1. Event-Driven Architecture
   - Warehouse system publishes "CostIncurred" events (labor, transport, storage)
   - Financial system subscribes and updates GL automatically
   - Queue (e.g., RabbitMQ) ensures no events are lost if systems are temporarily down

2. API-First Integration
   - Define contracts: What fields warehouse sends, GL expects?
   - Versioning: Allow financial system to evolve without breaking warehouse integration
   - Error handling: If GL is unavailable, warehouse still operates, retries later

3. Data Validation Layer
   - Validate cost amounts (negative values forbidden)
   - Validate accounts (cost code must exist in GL)
   - Validate authorization (only certain cost centers can create costs)

Critical Implementation Questions:
- What's the acceptable latency? (Real-time = complex, hourly = manageable)
- How should we handle failed transactions? (Retry? Manual intervention?)
- What happens if GL rejects a cost due to invalid account? (Who investigates?)

RISK MITIGATION:
- Start with batch integration (nightly), upgrade to real-time once stable
- Maintain reconciliation dashboard to catch discrepancies early
- Have rollback plan if integration causes GL corruption
```

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
```
IMPORTANCE:

1. Resource Allocation
   - Without forecasts: Always reactive (hire when peak arrives, fire during slowdown)
   - With forecasts: Proactive (train staff 2 months before peak season)
   Impact: 15-25% better labor cost management

2. Cash Flow Management
   - Finance needs to know Q3 capital expenses (e.g., warehouse expansion, automation)
   - Can't request funding if forecasting is unreliable
   
3. Performance Accountability
   - Compare actual vs budgeted costs
   - Identify deviations early
   - Manager bonus tied to staying within budget

4. Strategic Planning
   - Should we open new warehouse? Need growth forecast
   - Should we automate? Need cost/volume forecast
   - Should we enter new market? Need cost model for new region

DESIGNING AN ACCURATE FORECASTING SYSTEM:

Key Input Variables:
1. Historical Data (12-24 months minimum)
   - Order volume by product, warehouse, store
   - Labor hours per transaction
   - Transportation costs per mile/weight
   - Seasonal patterns (peak in Nov/Dec? Back-to-school in Aug?)

2. Forward-Looking Factors
   - Planned promotions (e.g., Black Friday = volume spike)
   - Staff hiring/attrition (new facility coming online?)
   - Market factors (economic growth, competitor activity)
   - Operational changes (new automation, process improvements)

Forecasting Methods:
- Time-series (simple): Last year's costs + growth rate
- Regression (better): Volume × cost per unit + fixed costs
- Machine Learning (advanced): Neural networks trained on historical patterns

Design Considerations:

1. Scenario Planning
   - Best case: +15% volume, no major issues
   - Base case: +8% volume, 1 facility shutdown for maintenance
   - Worst case: -5% volume, major storm disrupts transportation
   → Build budgets around base case, test risks with other scenarios

2. Rolling Forecasts (vs Fixed Annual Budgets)
   - Fixed: Budget set Jan 1 for full year (brittle, outdated by mid-year)
   - Rolling: Update forecast every month, always 12-month outlook (adaptive)
   → Rolling forecasts catch market changes faster

3. Variance Analysis & Learning
   - Track forecast accuracy: If forecast said $1M, actual was $950K = 5% error
   - Refine model based on variances
   - If we consistently underestimate transportation, adjust coefficients

4. Responsiveness Levels
   - Strategic forecasts (5 years): High-level, focuses on big trends
   - Tactical forecasts (12 months): Detailed, by warehouse/product
   - Operational forecasts (13 weeks): Daily/weekly, for staffing decisions
   → Different accuracies acceptable at different levels

CRITICAL QUESTIONS:
- How often do business conditions change? (Monthly? Weekly?)
- Do we have 2 years of historical data in consistent format?
- Which cost drivers are most volatile? (Focus forecasting effort there)
- Who owns forecasting accuracy? (Finance? Operations? Shared?)

RECOMMENDED IMPLEMENTATION APPROACH:

PHASE 1 (MVP - Months 1-2): Foundation
- Use simple linear regression: Total Cost = (Volume × Cost per Unit) + Fixed Costs
- Data source: Historical cost data (12-24 months) + sales forecast
- Tool: Excel with Power Query and basic formulas (low cost, familiar to team)
- Frequency: Monthly forecast update, 12-month rolling horizon
- Success metric: Forecast accuracy within ±10% of actual

PHASE 2 (Production - Months 3-4): Advanced
- Implement scenario planning (Best/Base/Worst case analysis)
- Separate forecasts by warehouse and product category
- Add seasonality adjustments based on historical patterns
- Tool: Tableau/Power BI for visualization or dedicated forecasting platform (Anaplan, Planful)
- Success metric: Forecast accuracy within ±5% of actual

PHASE 3 (Strategic - Months 5+): Optimization
- Machine learning models for demand and cost prediction
- Integration with operational systems (inventory, staffing)
- Automated exception alerts (deviation >threshold triggers investigation)
- Tool: Python/R with sklearn or cloud-based ML services (AWS Forecast, Azure Automated ML)
- Success metric: Proactive variance detection, forecast accuracy within ±3% of actual

HANDLING FORECAST ANOMALIES & BLACK SWAN EVENTS:
- Track "forecast miss" reasons (weather, promotions, market changes, data errors)
- Build separate model for volatile periods vs. normal operations
- Create escalation thresholds (deviation >15% requires investigation)
- Maintain emergency budget reserve (typically 10-15% of quarterly budget)

KEY INSIGHT:
Start simple (Phase 1) and evolve based on accuracy needs. Many companies overspend on forecasting 
tools early. Excel + discipline beats Anaplan + poor data governance.
```

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
```
WHY PRESERVE COST HISTORY:

1. Baseline for Budget Setting
   Old Warehouse cost patterns:
   - Labor: 500 staff-hours/day, $18/hour = $9K/day in labor costs
   - Transportation: $12K/month (fixed) + $0.50/unit variable
   - Storage: $50K/month
   
   New Warehouse expectations:
   - Should be 15% more efficient (better automation)
   - New budget: $40K/month (vs old $50K)
   
   → Can only set realistic targets if we know historical performance
   
   Without history: Guess budget = 50% chance of overspend/underutilization

2. Performance Attribution
   Problem: New warehouse underperforms budget by 20%
   
   Question: Is this because... (a) bad management? (b) bad forecast? (c) market conditions?
   
   If we have old warehouse data:
   - Market volume increased 10% → new warehouse actually outperforming on unit costs
   - Identifies root cause → can take corrective action
   
   Without history: Assume new warehouse is failing → blame wrong party

3. Trend Analysis
   - Old warehouse cost per unit was declining 3% annually (continuous improvement)
   - New warehouse should target 5% improvement (better processes)
   - Credible goal based on evidence

4. Regulatory & Audit Compliance
   - IRS, SOX require cost tracking consistency
   - "Why did warehouse X suddenly close?" needs documented explanation
   - Cost history = audit trail proving legitimacy of transition

COST CONTROL CONSIDERATIONS FOR REPLACEMENT:

Transition Costs (Often Overlooked):
- Overlap period: Operating both old & new warehouse during transition = double costs
- Data migration, staff training, ramp-up inefficiencies
- Plan: How long overlap needed? 1 week? 1 month? Affects total cost

Budget Setting Strategy:
1. Analyze old warehouse's stable-state costs (exclude startup/shutdown anomalies)
2. Adjust for known improvements (new automation, better layout)
3. Add transition contingency (typically 10-15% of quarterly budget)
4. Set new budget conservatively (expect 3-6 months to reach full efficiency)

Phased Transition Approach:
- Week 1-2: Run in parallel, monitor discrepancies
- Week 3-4: Shift 80% volume to new warehouse
- Week 5-6: Old warehouse operations minimal, cost should drop
- Week 7+: Archive old warehouse, evaluate transition cost

Key Metrics to Track During Transition:
- Cost per unit processed (old vs new)
- Labor utilization rate
- Order accuracy/damage rates
- Customer delivery times

Red Flags That Indicate Problems:
- New warehouse costs 25%+ higher than old (fundamental issue)
- Staff productivity drops >10% (training issues)
- Quality metrics degrade (process problems)

BEYOND COST: Strategic Questions to Ask when Replacing:
- Why replace? (Capacity? Age? Location? Technology?)
- What's the strategic objective? (Cost reduction? Service improvement? Consolidation?)
- Is this warehouse necessary long-term? (Or consolidate to other facilities?)
- How does this enable future growth?

PRACTICAL RECOMMENDATION:
Create a "transition scorecard" tracking:
- Cost variance (actual vs budget)
- Quality metrics (accuracy, damage)
- Service metrics (delivery time, inventory availability)
- Staff metrics (retention, productivity)

→ Provides early warning if replacement isn't delivering expected value
→ Decision point at month 3: Continue? Adjust? Escalate?

DATA ARCHIVAL STRATEGY:
When archiving the old warehouse, preserve cost history properly:

1. Archive Approach
   - Keep historical cost records in dedicated "archived warehouse" table in database
   - Link archived warehouse to new warehouse via Business Unit Code
   - Document archival date, reason, and transition metrics
   - Make data queryable for audits and trend analysis

2. Data Retention
   - Minimum retention: 5-7 years (regulatory compliance)
   - Keep at least 2 years in hot storage (easily accessible)
   - Move older data to cold storage (audit purposes)

3. Audit Trail
   - Record WHO archived warehouse, WHEN, and WHY
   - Maintain cost history for regulatory investigations (IRS, SOX audits)
   - Enable drill-down: Why did we close warehouse X? Cost history proves decision legitimacy

STAFF COMMUNICATION & CHANGE MANAGEMENT:
Warehouse replacement often triggers staff anxiety. Address proactively:

1. Communication Timeline
   - Month 1: Announcement of replacement (minimize rumors)
   - Month 2: Information sessions about new warehouse benefits and job security
   - Month 3-4: Training schedule and relocation details (if applicable)
   - Month 5+: Success stories from pilot, career progression opportunities

2. Staff Retention Strategy
   - Commit to "no layoffs" if possible (productivity increases, retention improves)
   - If consolidation required, offer severance + outplacement services
   - Highlight new roles: automation specialists, quality auditors, optimization analysts
   - Offer training for higher-value positions

3. Performance Management
   - Don't penalize staff for transition inefficiencies (ramp-up expected)
   - Measure individual performance vs. transition baseline (not vs. new warehouse steady-state)
   - Provide coaching for those struggling with new processes

CONTINGENCY PLANNING (If Month 3 Scorecard Shows Problems):

Scenario A: New Warehouse Costs 25%+ Higher
- Root cause: Poor layout? Inefficient processes? Unrealistic budget?
- Action: Bring in industrial engineer, conduct process audit
- Timeline: 2-week assessment, 4-week improvement plan
- Decision: Can we fix within 30 days? If not, escalate

Scenario B: Staff Productivity Down >10%
- Root cause: Training inadequate? Process confusion? Resistance?
- Action: Intensive training, process adjustment, morale improvement
- Timeline: Immediate retraining, 2-week reassessment
- Decision: Is productivity trend positive? If not, revisit transition approach

Scenario C: Quality Metrics Degraded (Damage Rate >5%)
- Root cause: Staff cutting corners? Process problems? Equipment issues?
- Action: Stop volume shifts, investigate root cause, quality stand-down
- Timeline: Immediate investigation, corrective action within 1 week
- Decision: Resume transition only after quality returns to baseline

Scenario D: Market Volume Changed Unexpectedly
- Root cause: Black swan event (recession, competitor action, supply chain disruption)
- Action: Revisit budget and timeline, may need extended overlap or different capacity
- Timeline: Monthly reassessment
- Decision: Does new warehouse still make economic sense?

KEY INSIGHT:
The warehouse replacement is not just a cost-cutting exercise—it's a business transformation. 
Success requires balancing cost control with staff retention, quality, and service. The transition 
scorecard gives you early warning to course-correct before problems become crises.
```

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
