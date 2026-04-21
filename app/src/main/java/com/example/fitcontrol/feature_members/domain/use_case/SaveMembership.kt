package com.example.fitcontrol.feature_members.domain.use_case

import com.example.fitcontrol.feature_members.domain.model.Membership
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import javax.inject.Inject

class SaveMembership @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(membership: Membership) {
        repository.insertMembership(membership)
    }
}